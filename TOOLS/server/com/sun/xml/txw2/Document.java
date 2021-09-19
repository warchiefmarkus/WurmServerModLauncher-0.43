/*     */ package com.sun.xml.txw2;
/*     */ 
/*     */ import com.sun.xml.txw2.output.XmlSerializer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Document
/*     */ {
/*     */   private final XmlSerializer out;
/*     */   private boolean started = false;
/*  51 */   private Content current = null;
/*     */   
/*  53 */   private final Map<Class, DatatypeWriter> datatypeWriters = (Map)new HashMap<Class<?>, DatatypeWriter>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private int iota = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private final NamespaceSupport inscopeNamespace = new NamespaceSupport();
/*     */ 
/*     */   
/*     */   private NamespaceDecl activeNamespaces;
/*     */ 
/*     */   
/*     */   private final ContentVisitor visitor;
/*     */   
/*     */   private final StringBuilder prefixSeed;
/*     */   
/*     */   private int prefixIota;
/*     */   
/*     */   static final char MAGIC = '\000';
/*     */ 
/*     */   
/*     */   void flush() {
/*  79 */     this.out.flush();
/*     */   }
/*     */   
/*     */   void setFirstContent(Content c) {
/*  83 */     assert this.current == null;
/*  84 */     this.current = new StartDocument();
/*  85 */     this.current.setNext(this, c);
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
/*     */   public void addDatatypeWriter(DatatypeWriter<?> dw) {
/* 100 */     this.datatypeWriters.put(dw.getType(), dw);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void run() {
/*     */     while (true) {
/* 108 */       Content next = this.current.getNext();
/* 109 */       if (next == null || !next.isReadyToCommit())
/*     */         return; 
/* 111 */       next.accept(this.visitor);
/* 112 */       next.written();
/* 113 */       this.current = next;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void writeValue(Object obj, NamespaceResolver nsResolver, StringBuilder buf) {
/* 124 */     if (obj == null) {
/* 125 */       throw new IllegalArgumentException("argument contains null");
/*     */     }
/* 127 */     if (obj instanceof Object[]) {
/* 128 */       for (Object o : (Object[])obj)
/* 129 */         writeValue(o, nsResolver, buf); 
/*     */       return;
/*     */     } 
/* 132 */     if (obj instanceof Iterable) {
/* 133 */       for (Object o : obj) {
/* 134 */         writeValue(o, nsResolver, buf);
/*     */       }
/*     */       return;
/*     */     } 
/* 138 */     if (buf.length() > 0) {
/* 139 */       buf.append(' ');
/*     */     }
/* 141 */     Class<?> c = obj.getClass();
/* 142 */     while (c != null) {
/* 143 */       DatatypeWriter<Object> dw = this.datatypeWriters.get(c);
/* 144 */       if (dw != null) {
/* 145 */         dw.print(obj, nsResolver, buf);
/*     */         return;
/*     */       } 
/* 148 */       c = c.getSuperclass();
/*     */     } 
/*     */ 
/*     */     
/* 152 */     buf.append(obj);
/*     */   }
/*     */   
/*     */   Document(XmlSerializer out) {
/* 156 */     this.visitor = new ContentVisitor()
/*     */       {
/*     */         
/*     */         public void onStartDocument()
/*     */         {
/* 161 */           throw new IllegalStateException();
/*     */         }
/*     */         
/*     */         public void onEndDocument() {
/* 165 */           Document.this.out.endDocument();
/*     */         }
/*     */         
/*     */         public void onEndTag() {
/* 169 */           Document.this.out.endTag();
/* 170 */           Document.this.inscopeNamespace.popContext();
/* 171 */           Document.this.activeNamespaces = null;
/*     */         }
/*     */         
/*     */         public void onPcdata(StringBuilder buffer) {
/* 175 */           if (Document.this.activeNamespaces != null)
/* 176 */             buffer = Document.this.fixPrefix(buffer); 
/* 177 */           Document.this.out.text(buffer);
/*     */         }
/*     */         
/*     */         public void onCdata(StringBuilder buffer) {
/* 181 */           if (Document.this.activeNamespaces != null)
/* 182 */             buffer = Document.this.fixPrefix(buffer); 
/* 183 */           Document.this.out.cdata(buffer);
/*     */         }
/*     */         
/*     */         public void onComment(StringBuilder buffer) {
/* 187 */           if (Document.this.activeNamespaces != null)
/* 188 */             buffer = Document.this.fixPrefix(buffer); 
/* 189 */           Document.this.out.comment(buffer);
/*     */         }
/*     */         
/*     */         public void onStartTag(String nsUri, String localName, Attribute attributes, NamespaceDecl namespaces) {
/* 193 */           assert nsUri != null;
/* 194 */           assert localName != null;
/*     */           
/* 196 */           Document.this.activeNamespaces = namespaces;
/*     */           
/* 198 */           if (!Document.this.started) {
/* 199 */             Document.this.started = true;
/* 200 */             Document.this.out.startDocument();
/*     */           } 
/*     */           
/* 203 */           Document.this.inscopeNamespace.pushContext();
/*     */           
/*     */           NamespaceDecl ns;
/* 206 */           for (ns = namespaces; ns != null; ns = ns.next) {
/* 207 */             ns.declared = false;
/*     */             
/* 209 */             if (ns.prefix != null) {
/* 210 */               String uri = Document.this.inscopeNamespace.getURI(ns.prefix);
/* 211 */               if (uri == null || !uri.equals(ns.uri)) {
/*     */ 
/*     */ 
/*     */                 
/* 215 */                 Document.this.inscopeNamespace.declarePrefix(ns.prefix, ns.uri);
/* 216 */                 ns.declared = true;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 222 */           for (ns = namespaces; ns != null; ns = ns.next) {
/* 223 */             if (ns.prefix == null) {
/* 224 */               if (Document.this.inscopeNamespace.getURI("").equals(ns.uri)) {
/* 225 */                 ns.prefix = "";
/*     */               } else {
/* 227 */                 String p = Document.this.inscopeNamespace.getPrefix(ns.uri);
/* 228 */                 if (p == null) {
/*     */                   
/* 230 */                   while (Document.this.inscopeNamespace.getURI(p = Document.this.newPrefix()) != null);
/*     */                   
/* 232 */                   ns.declared = true;
/* 233 */                   Document.this.inscopeNamespace.declarePrefix(p, ns.uri);
/*     */                 } 
/* 235 */                 ns.prefix = p;
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 241 */           assert namespaces.uri.equals(nsUri);
/* 242 */           assert namespaces.prefix != null : "a prefix must have been all allocated";
/* 243 */           Document.this.out.beginStartTag(nsUri, localName, namespaces.prefix);
/*     */ 
/*     */           
/* 246 */           for (ns = namespaces; ns != null; ns = ns.next) {
/* 247 */             if (ns.declared) {
/* 248 */               Document.this.out.writeXmlns(ns.prefix, ns.uri);
/*     */             }
/*     */           } 
/*     */           
/* 252 */           for (Attribute a = attributes; a != null; a = a.next) {
/*     */             String prefix;
/* 254 */             if (a.nsUri.length() == 0) { prefix = ""; }
/* 255 */             else { prefix = Document.this.inscopeNamespace.getPrefix(a.nsUri); }
/* 256 */              Document.this.out.writeAttribute(a.nsUri, a.localName, prefix, Document.this.fixPrefix(a.value));
/*     */           } 
/*     */           
/* 259 */           Document.this.out.endStartTag(nsUri, localName, namespaces.prefix);
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     this.prefixSeed = new StringBuilder("ns");
/*     */     
/* 268 */     this.prefixIota = 0;
/*     */     this.out = out;
/*     */     for (DatatypeWriter<?> dw : DatatypeWriter.BUILDIN)
/*     */       this.datatypeWriters.put(dw.getType(), dw); 
/*     */   }
/*     */   private String newPrefix() {
/* 274 */     this.prefixSeed.setLength(2);
/* 275 */     this.prefixSeed.append(++this.prefixIota);
/* 276 */     return this.prefixSeed.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private StringBuilder fixPrefix(StringBuilder buf) {
/* 287 */     assert this.activeNamespaces != null;
/*     */ 
/*     */     
/* 290 */     int len = buf.length(); int i;
/* 291 */     for (i = 0; i < len && 
/* 292 */       buf.charAt(i) != '\000'; i++);
/*     */ 
/*     */ 
/*     */     
/* 296 */     if (i == len) {
/* 297 */       return buf;
/*     */     }
/* 299 */     while (i < len) {
/* 300 */       char uriIdx = buf.charAt(i + 1);
/* 301 */       NamespaceDecl ns = this.activeNamespaces;
/* 302 */       while (ns != null && ns.uniqueId != uriIdx)
/* 303 */         ns = ns.next; 
/* 304 */       if (ns == null) {
/* 305 */         throw new IllegalStateException("Unexpected use of prefixes " + buf);
/*     */       }
/* 307 */       int length = 2;
/* 308 */       String prefix = ns.prefix;
/* 309 */       if (prefix.length() == 0) {
/* 310 */         if (buf.length() <= i + 2 || buf.charAt(i + 2) != ':')
/* 311 */           throw new IllegalStateException("Unexpected use of prefixes " + buf); 
/* 312 */         length = 3;
/*     */       } 
/*     */       
/* 315 */       buf.replace(i, i + length, prefix);
/* 316 */       len += prefix.length() - length;
/*     */       
/* 318 */       while (i < len && buf.charAt(i) != '\000') {
/* 319 */         i++;
/*     */       }
/*     */     } 
/* 322 */     return buf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   char assignNewId() {
/* 331 */     return (char)this.iota++;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\Document.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.xml.bind.v2.runtime.output;
/*     */ 
/*     */ import com.sun.istack.NotNull;
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
/*     */ import com.sun.xml.bind.v2.runtime.Name;
/*     */ import com.sun.xml.bind.v2.runtime.NamespaceContext2;
/*     */ import com.sun.xml.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NamespaceContextImpl
/*     */   implements NamespaceContext2
/*     */ {
/*     */   private final XMLSerializer owner;
/*  68 */   private String[] prefixes = new String[4];
/*  69 */   private String[] nsUris = new String[4];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Element current;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Element top;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   private NamespacePrefixMapper prefixMapper = defaultNamespacePrefixMapper;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean collectionMode;
/*     */ 
/*     */ 
/*     */   
/*     */   public NamespaceContextImpl(XMLSerializer owner) {
/* 123 */     this.owner = owner;
/*     */     
/* 125 */     this.current = this.top = new Element(this, null);
/*     */     
/* 127 */     put("http://www.w3.org/XML/1998/namespace", "xml");
/*     */   }
/*     */   
/*     */   public void setPrefixMapper(NamespacePrefixMapper mapper) {
/* 131 */     if (mapper == null)
/* 132 */       mapper = defaultNamespacePrefixMapper; 
/* 133 */     this.prefixMapper = mapper;
/*     */   }
/*     */   
/*     */   public NamespacePrefixMapper getPrefixMapper() {
/* 137 */     return this.prefixMapper;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 141 */     this.current = this.top;
/* 142 */     this.size = 1;
/* 143 */     this.collectionMode = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int declareNsUri(String uri, String preferedPrefix, boolean requirePrefix) {
/* 151 */     preferedPrefix = this.prefixMapper.getPreferredPrefix(uri, preferedPrefix, requirePrefix);
/*     */     
/* 153 */     if (uri.length() == 0) {
/* 154 */       for (int j = this.size - 1; j >= 0; j--) {
/* 155 */         if (this.nsUris[j].length() == 0)
/* 156 */           return j; 
/* 157 */         if (this.prefixes[j].length() == 0) {
/*     */ 
/*     */           
/* 160 */           assert this.current.defaultPrefixIndex == -1 && this.current.oldDefaultNamespaceUriIndex == -1;
/*     */           
/* 162 */           String oldUri = this.nsUris[j];
/* 163 */           String[] knownURIs = this.owner.nameList.namespaceURIs;
/*     */           
/* 165 */           if (this.current.baseIndex <= j) {
/*     */ 
/*     */             
/* 168 */             this.nsUris[j] = "";
/*     */             
/* 170 */             int subst = put(oldUri, null);
/*     */ 
/*     */             
/* 173 */             for (int m = knownURIs.length - 1; m >= 0; m--) {
/* 174 */               if (knownURIs[m].equals(oldUri)) {
/* 175 */                 this.owner.knownUri2prefixIndexMap[m] = subst;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 180 */             return j;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 185 */           for (int k = knownURIs.length - 1; k >= 0; k--) {
/* 186 */             if (knownURIs[k].equals(oldUri)) {
/* 187 */               this.current.defaultPrefixIndex = j;
/* 188 */               this.current.oldDefaultNamespaceUriIndex = k;
/* 189 */               assert this.owner.knownUri2prefixIndexMap[k] == this.current.defaultPrefixIndex;
/*     */               
/* 191 */               this.owner.knownUri2prefixIndexMap[k] = this.size;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 196 */           put(this.nsUris[j], null);
/* 197 */           return put("", "");
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 203 */       return put("", "");
/*     */     } 
/*     */     
/* 206 */     for (int i = this.size - 1; i >= 0; i--) {
/* 207 */       String p = this.prefixes[i];
/* 208 */       if (this.nsUris[i].equals(uri) && (
/* 209 */         !requirePrefix || p.length() > 0)) {
/* 210 */         return i;
/*     */       }
/*     */       
/* 213 */       if (p.equals(preferedPrefix))
/*     */       {
/* 215 */         preferedPrefix = null;
/*     */       }
/*     */     } 
/*     */     
/* 219 */     if (preferedPrefix == null && requirePrefix)
/*     */     {
/*     */       
/* 222 */       preferedPrefix = makeUniquePrefix();
/*     */     }
/*     */ 
/*     */     
/* 226 */     return put(uri, preferedPrefix);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int force(@NotNull String uri, @NotNull String prefix) {
/* 232 */     for (int i = this.size - 1; i >= 0; i--) {
/* 233 */       if (this.prefixes[i].equals(prefix)) {
/* 234 */         if (this.nsUris[i].equals(uri)) {
/* 235 */           return i;
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     return put(uri, prefix);
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
/*     */   public int put(@NotNull String uri, @Nullable String prefix) {
/* 258 */     if (this.size == this.nsUris.length) {
/*     */       
/* 260 */       String[] u = new String[this.nsUris.length * 2];
/* 261 */       String[] p = new String[this.prefixes.length * 2];
/* 262 */       System.arraycopy(this.nsUris, 0, u, 0, this.nsUris.length);
/* 263 */       System.arraycopy(this.prefixes, 0, p, 0, this.prefixes.length);
/* 264 */       this.nsUris = u;
/* 265 */       this.prefixes = p;
/*     */     } 
/* 267 */     if (prefix == null) {
/* 268 */       if (this.size == 1) {
/* 269 */         prefix = "";
/*     */       } else {
/*     */         
/* 272 */         prefix = makeUniquePrefix();
/*     */       } 
/*     */     }
/* 275 */     this.nsUris[this.size] = uri;
/* 276 */     this.prefixes[this.size] = prefix;
/*     */     
/* 278 */     return this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   private String makeUniquePrefix() {
/* 283 */     String prefix = (new StringBuilder(5)).append("ns").append(this.size).toString();
/* 284 */     while (getNamespaceURI(prefix) != null) {
/* 285 */       prefix = prefix + '_';
/*     */     }
/* 287 */     return prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public Element getCurrent() {
/* 292 */     return this.current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrefixIndex(String uri) {
/* 300 */     for (int i = this.size - 1; i >= 0; i--) {
/* 301 */       if (this.nsUris[i].equals(uri))
/* 302 */         return i; 
/*     */     } 
/* 304 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix(int prefixIndex) {
/* 313 */     return this.prefixes[prefixIndex];
/*     */   }
/*     */   
/*     */   public String getNamespaceURI(int prefixIndex) {
/* 317 */     return this.nsUris[prefixIndex];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespaceURI(String prefix) {
/* 327 */     for (int i = this.size - 1; i >= 0; i--) {
/* 328 */       if (this.prefixes[i].equals(prefix))
/* 329 */         return this.nsUris[i]; 
/* 330 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefix(String uri) {
/* 338 */     if (this.collectionMode) {
/* 339 */       return declareNamespace(uri, null, false);
/*     */     }
/* 341 */     for (int i = this.size - 1; i >= 0; i--) {
/* 342 */       if (this.nsUris[i].equals(uri))
/* 343 */         return this.prefixes[i]; 
/* 344 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<String> getPrefixes(String uri) {
/* 349 */     String prefix = getPrefix(uri);
/* 350 */     if (prefix == null) {
/* 351 */       return Collections.<String>emptySet().iterator();
/*     */     }
/* 353 */     return Collections.<String>singleton(uri).iterator();
/*     */   }
/*     */   
/*     */   public String declareNamespace(String namespaceUri, String preferedPrefix, boolean requirePrefix) {
/* 357 */     int idx = declareNsUri(namespaceUri, preferedPrefix, requirePrefix);
/* 358 */     return getPrefix(idx);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int count() {
/* 365 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final class Element
/*     */   {
/*     */     public final NamespaceContextImpl context;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Element prev;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Element next;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int oldDefaultNamespaceUriIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int defaultPrefixIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int baseIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int depth;
/*     */ 
/*     */ 
/*     */     
/*     */     private int elementNamePrefix;
/*     */ 
/*     */ 
/*     */     
/*     */     private String elementLocalName;
/*     */ 
/*     */ 
/*     */     
/*     */     private Name elementName;
/*     */ 
/*     */ 
/*     */     
/*     */     private Object outerPeer;
/*     */ 
/*     */ 
/*     */     
/*     */     private Object innerPeer;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Element(NamespaceContextImpl context, Element prev) {
/* 430 */       this.context = context;
/* 431 */       this.prev = prev;
/* 432 */       this.depth = (prev == null) ? 0 : (prev.depth + 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRootElement() {
/* 440 */       return (this.depth == 1);
/*     */     }
/*     */     
/*     */     public Element push() {
/* 444 */       if (this.next == null)
/* 445 */         this.next = new Element(this.context, this); 
/* 446 */       this.next.onPushed();
/* 447 */       return this.next;
/*     */     }
/*     */     
/*     */     public Element pop() {
/* 451 */       if (this.oldDefaultNamespaceUriIndex >= 0)
/*     */       {
/* 453 */         this.context.owner.knownUri2prefixIndexMap[this.oldDefaultNamespaceUriIndex] = this.defaultPrefixIndex;
/*     */       }
/* 455 */       this.context.size = this.baseIndex;
/* 456 */       this.context.current = this.prev;
/* 457 */       return this.prev;
/*     */     }
/*     */     
/*     */     private void onPushed() {
/* 461 */       this.oldDefaultNamespaceUriIndex = this.defaultPrefixIndex = -1;
/* 462 */       this.baseIndex = this.context.size;
/* 463 */       this.context.current = this;
/*     */     }
/*     */     
/*     */     public void setTagName(int prefix, String localName, Object outerPeer) {
/* 467 */       assert localName != null;
/* 468 */       this.elementNamePrefix = prefix;
/* 469 */       this.elementLocalName = localName;
/* 470 */       this.elementName = null;
/* 471 */       this.outerPeer = outerPeer;
/*     */     }
/*     */     
/*     */     public void setTagName(Name tagName, Object outerPeer) {
/* 475 */       assert tagName != null;
/* 476 */       this.elementName = tagName;
/* 477 */       this.outerPeer = outerPeer;
/*     */     }
/*     */     
/*     */     public void startElement(XmlOutput out, Object innerPeer) throws IOException, XMLStreamException {
/* 481 */       this.innerPeer = innerPeer;
/* 482 */       if (this.elementName != null) {
/* 483 */         out.beginStartTag(this.elementName);
/*     */       } else {
/* 485 */         out.beginStartTag(this.elementNamePrefix, this.elementLocalName);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endElement(XmlOutput out) throws IOException, SAXException, XMLStreamException {
/* 490 */       if (this.elementName != null) {
/* 491 */         out.endTag(this.elementName);
/* 492 */         this.elementName = null;
/*     */       } else {
/* 494 */         out.endTag(this.elementNamePrefix, this.elementLocalName);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final int count() {
/* 502 */       return this.context.size - this.baseIndex;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getPrefix(int idx) {
/* 512 */       return this.context.prefixes[this.baseIndex + idx];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getNsUri(int idx) {
/* 522 */       return this.context.nsUris[this.baseIndex + idx];
/*     */     }
/*     */     
/*     */     public int getBase() {
/* 526 */       return this.baseIndex;
/*     */     }
/*     */     
/*     */     public Object getOuterPeer() {
/* 530 */       return this.outerPeer;
/*     */     }
/*     */     
/*     */     public Object getInnerPeer() {
/* 534 */       return this.innerPeer;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Element getParent() {
/* 541 */       return this.prev;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 550 */   private static final NamespacePrefixMapper defaultNamespacePrefixMapper = new NamespacePrefixMapper() {
/*     */       public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
/* 552 */         if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance"))
/* 553 */           return "xsi"; 
/* 554 */         if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema"))
/* 555 */           return "xs"; 
/* 556 */         if (namespaceUri.equals("http://www.w3.org/2005/05/xmlmime"))
/* 557 */           return "xmime"; 
/* 558 */         return suggestion;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\output\NamespaceContextImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
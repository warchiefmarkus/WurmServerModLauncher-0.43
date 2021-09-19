/*     */ package com.sun.xml.txw2;
/*     */ 
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class NamespaceSupport
/*     */ {
/*     */   public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
/*     */   public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
/* 136 */   private static final Enumeration EMPTY_ENUMERATION = (new Vector()).elements();
/*     */ 
/*     */   
/*     */   private Context[] contexts;
/*     */ 
/*     */   
/*     */   private Context currentContext;
/*     */   
/*     */   private int contextPos;
/*     */   
/*     */   private boolean namespaceDeclUris;
/*     */ 
/*     */   
/*     */   public NamespaceSupport() {
/* 150 */     reset();
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
/*     */   public void reset() {
/* 173 */     this.contexts = new Context[32];
/* 174 */     this.namespaceDeclUris = false;
/* 175 */     this.contextPos = 0;
/* 176 */     this.contexts[this.contextPos] = this.currentContext = new Context();
/* 177 */     this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushContext() {
/* 219 */     int max = this.contexts.length;
/*     */     
/* 221 */     this.contextPos++;
/*     */ 
/*     */     
/* 224 */     if (this.contextPos >= max) {
/* 225 */       Context[] newContexts = new Context[max * 2];
/* 226 */       System.arraycopy(this.contexts, 0, newContexts, 0, max);
/* 227 */       max *= 2;
/* 228 */       this.contexts = newContexts;
/*     */     } 
/*     */ 
/*     */     
/* 232 */     this.currentContext = this.contexts[this.contextPos];
/* 233 */     if (this.currentContext == null) {
/* 234 */       this.contexts[this.contextPos] = this.currentContext = new Context();
/*     */     }
/*     */ 
/*     */     
/* 238 */     if (this.contextPos > 0) {
/* 239 */       this.currentContext.setParent(this.contexts[this.contextPos - 1]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void popContext() {
/* 259 */     this.contexts[this.contextPos].clear();
/* 260 */     this.contextPos--;
/* 261 */     if (this.contextPos < 0) {
/* 262 */       throw new EmptyStackException();
/*     */     }
/* 264 */     this.currentContext = this.contexts[this.contextPos];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean declarePrefix(String prefix, String uri) {
/* 309 */     if (prefix.equals("xml") || prefix.equals("xmlns")) {
/* 310 */       return false;
/*     */     }
/* 312 */     this.currentContext.declarePrefix(prefix, uri);
/* 313 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] processName(String qName, String[] parts, boolean isAttribute) {
/* 361 */     String[] myParts = this.currentContext.processName(qName, isAttribute);
/* 362 */     if (myParts == null) {
/* 363 */       return null;
/*     */     }
/* 365 */     parts[0] = myParts[0];
/* 366 */     parts[1] = myParts[1];
/* 367 */     parts[2] = myParts[2];
/* 368 */     return parts;
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
/*     */   public String getURI(String prefix) {
/* 387 */     return this.currentContext.getURI(prefix);
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
/*     */   public Enumeration getPrefixes() {
/* 407 */     return this.currentContext.getPrefixes();
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
/*     */   
/*     */   public String getPrefix(String uri) {
/* 432 */     return this.currentContext.getPrefix(uri);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration getPrefixes(String uri) {
/* 461 */     Vector<String> prefixes = new Vector();
/* 462 */     Enumeration<String> allPrefixes = getPrefixes();
/* 463 */     while (allPrefixes.hasMoreElements()) {
/* 464 */       String prefix = allPrefixes.nextElement();
/* 465 */       if (uri.equals(getURI(prefix))) {
/* 466 */         prefixes.addElement(prefix);
/*     */       }
/*     */     } 
/* 469 */     return prefixes.elements();
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
/*     */   public Enumeration getDeclaredPrefixes() {
/* 487 */     return this.currentContext.getDeclaredPrefixes();
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
/*     */   public void setNamespaceDeclUris(boolean value) {
/* 503 */     if (this.contextPos != 0)
/* 504 */       throw new IllegalStateException(); 
/* 505 */     if (value == this.namespaceDeclUris)
/*     */       return; 
/* 507 */     this.namespaceDeclUris = value;
/* 508 */     if (value) {
/* 509 */       this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
/*     */     } else {
/* 511 */       this.contexts[this.contextPos] = this.currentContext = new Context();
/* 512 */       this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNamespaceDeclUris() {
/* 523 */     return this.namespaceDeclUris;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final class Context
/*     */   {
/*     */     Hashtable prefixTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Hashtable uriTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Hashtable elementNameTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Hashtable attributeNameTable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String defaultNS;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Vector declarations;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean declSeen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Context parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Context() {
/* 824 */       this.defaultNS = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 832 */       this.declarations = null;
/* 833 */       this.declSeen = false;
/* 834 */       this.parent = null;
/*     */       copyTables();
/*     */     }
/*     */     
/*     */     void setParent(Context parent) {
/*     */       this.parent = parent;
/*     */       this.declarations = null;
/*     */       this.prefixTable = parent.prefixTable;
/*     */       this.uriTable = parent.uriTable;
/*     */       this.elementNameTable = parent.elementNameTable;
/*     */       this.attributeNameTable = parent.attributeNameTable;
/*     */       this.defaultNS = parent.defaultNS;
/*     */       this.declSeen = false;
/*     */     }
/*     */     
/*     */     void clear() {
/*     */       this.parent = null;
/*     */       this.prefixTable = null;
/*     */       this.uriTable = null;
/*     */       this.elementNameTable = null;
/*     */       this.attributeNameTable = null;
/*     */       this.defaultNS = "";
/*     */     }
/*     */     
/*     */     void declarePrefix(String prefix, String uri) {
/*     */       if (!this.declSeen)
/*     */         copyTables(); 
/*     */       if (this.declarations == null)
/*     */         this.declarations = new Vector(); 
/*     */       prefix = prefix.intern();
/*     */       uri = uri.intern();
/*     */       if ("".equals(prefix)) {
/*     */         this.defaultNS = uri;
/*     */       } else {
/*     */         this.prefixTable.put(prefix, uri);
/*     */         this.uriTable.put(uri, prefix);
/*     */       } 
/*     */       this.declarations.addElement(prefix);
/*     */     }
/*     */     
/*     */     String[] processName(String qName, boolean isAttribute) {
/*     */       Hashtable<String, String[]> table;
/*     */       if (isAttribute) {
/*     */         table = this.attributeNameTable;
/*     */       } else {
/*     */         table = this.elementNameTable;
/*     */       } 
/*     */       String[] name = (String[])table.get(qName);
/*     */       if (name != null)
/*     */         return name; 
/*     */       name = new String[3];
/*     */       name[2] = qName.intern();
/*     */       int index = qName.indexOf(':');
/*     */       if (index == -1) {
/*     */         if (isAttribute) {
/*     */           if (qName == "xmlns" && NamespaceSupport.this.namespaceDeclUris) {
/*     */             name[0] = "http://www.w3.org/xmlns/2000/";
/*     */           } else {
/*     */             name[0] = "";
/*     */           } 
/*     */         } else {
/*     */           name[0] = this.defaultNS;
/*     */         } 
/*     */         name[1] = name[2];
/*     */       } else {
/*     */         String uri, prefix = qName.substring(0, index);
/*     */         String local = qName.substring(index + 1);
/*     */         if ("".equals(prefix)) {
/*     */           uri = this.defaultNS;
/*     */         } else {
/*     */           uri = (String)this.prefixTable.get(prefix);
/*     */         } 
/*     */         if (uri == null || (!isAttribute && "xmlns".equals(prefix)))
/*     */           return null; 
/*     */         name[0] = uri;
/*     */         name[1] = local.intern();
/*     */       } 
/*     */       table.put(name[2], name);
/*     */       return name;
/*     */     }
/*     */     
/*     */     String getURI(String prefix) {
/*     */       if ("".equals(prefix))
/*     */         return this.defaultNS; 
/*     */       if (this.prefixTable == null)
/*     */         return null; 
/*     */       return (String)this.prefixTable.get(prefix);
/*     */     }
/*     */     
/*     */     String getPrefix(String uri) {
/*     */       if (this.uriTable == null)
/*     */         return null; 
/*     */       return (String)this.uriTable.get(uri);
/*     */     }
/*     */     
/*     */     Enumeration getDeclaredPrefixes() {
/*     */       if (this.declarations == null)
/*     */         return NamespaceSupport.EMPTY_ENUMERATION; 
/*     */       return this.declarations.elements();
/*     */     }
/*     */     
/*     */     Enumeration getPrefixes() {
/*     */       if (this.prefixTable == null)
/*     */         return NamespaceSupport.EMPTY_ENUMERATION; 
/*     */       return this.prefixTable.keys();
/*     */     }
/*     */     
/*     */     private void copyTables() {
/*     */       if (this.prefixTable != null) {
/*     */         this.prefixTable = (Hashtable)this.prefixTable.clone();
/*     */       } else {
/*     */         this.prefixTable = new Hashtable<Object, Object>();
/*     */       } 
/*     */       if (this.uriTable != null) {
/*     */         this.uriTable = (Hashtable)this.uriTable.clone();
/*     */       } else {
/*     */         this.uriTable = new Hashtable<Object, Object>();
/*     */       } 
/*     */       this.elementNameTable = new Hashtable<Object, Object>();
/*     */       this.attributeNameTable = new Hashtable<Object, Object>();
/*     */       this.declSeen = true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\NamespaceSupport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
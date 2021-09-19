/*     */ package com.sun.xml.txw2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StartTag
/*     */   extends Content
/*     */   implements NamespaceResolver
/*     */ {
/*     */   private String uri;
/*     */   private final String localName;
/*     */   private Attribute firstAtt;
/*     */   private Attribute lastAtt;
/*     */   private ContainerElement owner;
/*     */   private NamespaceDecl firstNs;
/*     */   private NamespaceDecl lastNs;
/*     */   final Document document;
/*     */   
/*     */   public StartTag(ContainerElement owner, String uri, String localName) {
/*  71 */     this(owner.document, uri, localName);
/*  72 */     this.owner = owner;
/*     */   }
/*     */   
/*     */   public StartTag(Document document, String uri, String localName) {
/*  76 */     assert uri != null;
/*  77 */     assert localName != null;
/*     */     
/*  79 */     this.uri = uri;
/*  80 */     this.localName = localName;
/*  81 */     this.document = document;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     addNamespaceDecl(uri, null, false);
/*     */   }
/*     */   
/*     */   public void addAttribute(String nsUri, String localName, Object arg) {
/*  90 */     checkWritable();
/*     */     
/*     */     Attribute a;
/*     */     
/*  94 */     for (a = this.firstAtt; a != null && 
/*  95 */       !a.hasName(nsUri, localName); a = a.next);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     if (a == null) {
/* 102 */       a = new Attribute(nsUri, localName);
/* 103 */       if (this.lastAtt == null) {
/* 104 */         assert this.firstAtt == null;
/* 105 */         this.firstAtt = this.lastAtt = a;
/*     */       } else {
/* 107 */         assert this.firstAtt != null;
/* 108 */         this.lastAtt.next = a;
/* 109 */         this.lastAtt = a;
/*     */       } 
/* 111 */       if (nsUri.length() > 0) {
/* 112 */         addNamespaceDecl(nsUri, null, true);
/*     */       }
/*     */     } 
/* 115 */     this.document.writeValue(arg, this, a.value);
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
/*     */   public NamespaceDecl addNamespaceDecl(String uri, String prefix, boolean requirePrefix) {
/* 138 */     checkWritable();
/*     */     
/* 140 */     if (uri == null)
/* 141 */       throw new IllegalArgumentException(); 
/* 142 */     if (uri.length() == 0) {
/* 143 */       if (requirePrefix)
/* 144 */         throw new IllegalArgumentException("The empty namespace cannot have a non-empty prefix"); 
/* 145 */       if (prefix != null && prefix.length() > 0)
/* 146 */         throw new IllegalArgumentException("The empty namespace can be only bound to the empty prefix"); 
/* 147 */       prefix = "";
/*     */     } 
/*     */ 
/*     */     
/* 151 */     for (NamespaceDecl n = this.firstNs; n != null; n = n.next) {
/* 152 */       if (uri.equals(n.uri)) {
/* 153 */         if (prefix == null) {
/*     */           
/* 155 */           n.requirePrefix |= requirePrefix;
/* 156 */           return n;
/*     */         } 
/* 158 */         if (n.prefix == null) {
/*     */           
/* 160 */           n.prefix = prefix;
/* 161 */           n.requirePrefix |= requirePrefix;
/* 162 */           return n;
/*     */         } 
/* 164 */         if (prefix.equals(n.prefix)) {
/*     */           
/* 166 */           n.requirePrefix |= requirePrefix;
/* 167 */           return n;
/*     */         } 
/*     */       } 
/* 170 */       if (prefix != null && n.prefix != null && n.prefix.equals(prefix)) {
/* 171 */         throw new IllegalArgumentException("Prefix '" + prefix + "' is already bound to '" + n.uri + '\'');
/*     */       }
/*     */     } 
/*     */     
/* 175 */     NamespaceDecl ns = new NamespaceDecl(this.document.assignNewId(), uri, prefix, requirePrefix);
/* 176 */     if (this.lastNs == null) {
/* 177 */       assert this.firstNs == null;
/* 178 */       this.firstNs = this.lastNs = ns;
/*     */     } else {
/* 180 */       assert this.firstNs != null;
/* 181 */       this.lastNs.next = ns;
/* 182 */       this.lastNs = ns;
/*     */     } 
/* 184 */     return ns;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkWritable() {
/* 191 */     if (isWritten()) {
/* 192 */       throw new IllegalStateException("The start tag of " + this.localName + " has already been written. " + "If you need out of order writing, see the TypedXmlWriter.block method");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isWritten() {
/* 201 */     return (this.uri == null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isReadyToCommit() {
/* 209 */     if (this.owner != null && this.owner.isBlocked()) {
/* 210 */       return false;
/*     */     }
/* 212 */     for (Content c = getNext(); c != null; c = c.getNext()) {
/* 213 */       if (c.concludesPendingStartTag())
/* 214 */         return true; 
/*     */     } 
/* 216 */     return false;
/*     */   }
/*     */   
/*     */   public void written() {
/* 220 */     this.firstAtt = this.lastAtt = null;
/* 221 */     this.uri = null;
/* 222 */     if (this.owner != null) {
/* 223 */       assert this.owner.startTag == this;
/* 224 */       this.owner.startTag = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean concludesPendingStartTag() {
/* 229 */     return true;
/*     */   }
/*     */   
/*     */   void accept(ContentVisitor visitor) {
/* 233 */     visitor.onStartTag(this.uri, this.localName, this.firstAtt, this.firstNs);
/*     */   }
/*     */   
/*     */   public String getPrefix(String nsUri) {
/* 237 */     NamespaceDecl ns = addNamespaceDecl(nsUri, null, false);
/* 238 */     if (ns.prefix != null)
/*     */     {
/* 240 */       return ns.prefix; } 
/* 241 */     return ns.dummyPrefix;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\txw2\StartTag.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
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
/*     */ class importDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String ns;
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCRuntimeEx runtime) {
/*  39 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  44 */     if (this.ns == null) this.ns = ""; 
/*  45 */     this.$runtime.importSchema(this.ns, this.schemaLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  51 */     this.$uri = $__uri;
/*  52 */     this.$localName = $__local;
/*  53 */     this.$qname = $__qname;
/*  54 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/*  57 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*  58 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  59 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/*  62 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  68 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  69 */           this.$runtime.consumeAttribute($ai);
/*  70 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  73 */           this.$_ngcc_current_state = 2;
/*  74 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  80 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  81 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 3, null, AnnotationContext.SCHEMA);
/*  82 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           this.$_ngcc_current_state = 1;
/*  86 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/*  92 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  93 */           this.$runtime.consumeAttribute($ai);
/*  94 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  97 */           this.$_ngcc_current_state = 4;
/*  98 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 104 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 109 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 117 */     this.$uri = $__uri;
/* 118 */     this.$localName = $__local;
/* 119 */     this.$qname = $__qname;
/* 120 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 123 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 124 */           this.$runtime.consumeAttribute($ai);
/* 125 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 128 */           this.$_ngcc_current_state = 2;
/* 129 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 135 */         this.$_ngcc_current_state = 1;
/* 136 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 141 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 142 */           this.$runtime.consumeAttribute($ai);
/* 143 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 146 */           this.$_ngcc_current_state = 4;
/* 147 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 153 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/* 154 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 155 */           this.$_ngcc_current_state = 0;
/* 156 */           action0();
/*     */         } else {
/*     */           
/* 159 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 165 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 170 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 178 */     this.$uri = $__uri;
/* 179 */     this.$localName = $__local;
/* 180 */     this.$qname = $__qname;
/* 181 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 184 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 185 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 188 */           this.$_ngcc_current_state = 2;
/* 189 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 195 */         this.$_ngcc_current_state = 1;
/* 196 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 201 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 202 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 205 */           this.$_ngcc_current_state = 4;
/* 206 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 212 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 217 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 225 */     this.$uri = $__uri;
/* 226 */     this.$localName = $__local;
/* 227 */     this.$qname = $__qname;
/* 228 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 231 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 232 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 235 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 241 */         this.$_ngcc_current_state = 2;
/* 242 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 247 */         this.$_ngcc_current_state = 1;
/* 248 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 253 */         this.$_ngcc_current_state = 4;
/* 254 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 259 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 260 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 263 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 269 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 274 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 282 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 285 */         this.ns = $value;
/* 286 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 291 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 292 */           this.$runtime.consumeAttribute($ai);
/* 293 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 296 */         this.$_ngcc_current_state = 2;
/* 297 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 6:
/* 303 */         this.schemaLocation = $value;
/* 304 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 309 */         this.$_ngcc_current_state = 1;
/* 310 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 315 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 316 */           this.$runtime.consumeAttribute($ai);
/* 317 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 320 */         this.$_ngcc_current_state = 4;
/* 321 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 327 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 334 */     switch ($__cookie__) {
/*     */       
/*     */       case 3:
/* 337 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 344 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\importDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
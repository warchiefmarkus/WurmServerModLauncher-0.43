/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.HashSet;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class wildcardBody
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private Locator locator;
/*     */   private String modeValue;
/*     */   private String ns;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  34 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public wildcardBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator) {
/*  38 */     super(source, parent, cookie);
/*  39 */     this.$runtime = runtime;
/*  40 */     this.locator = _locator;
/*  41 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */   
/*     */   public wildcardBody(NGCCRuntimeEx runtime, Locator _locator) {
/*  45 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _locator);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  50 */     this.$uri = $__uri;
/*  51 */     this.$localName = $__local;
/*  52 */     this.$qname = $__qname;
/*  53 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/*  56 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  57 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 556, null, AnnotationContext.WILDCARD);
/*  58 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  61 */           this.$_ngcc_current_state = 9;
/*  62 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  68 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  73 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  74 */           this.$runtime.consumeAttribute($ai);
/*  75 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  78 */           this.$_ngcc_current_state = 0;
/*  79 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/*  85 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  86 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/*  87 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  90 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/*  91 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  97 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  98 */           this.$runtime.consumeAttribute($ai);
/*  99 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 102 */           this.$_ngcc_current_state = 1;
/* 103 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
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
/*     */       case 10:
/* 123 */         this.$_ngcc_current_state = 9;
/* 124 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 129 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 134 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 135 */           this.$runtime.consumeAttribute($ai);
/* 136 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 139 */           this.$_ngcc_current_state = 0;
/* 140 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 146 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 147 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 148 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 151 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 152 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 158 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 159 */           this.$runtime.consumeAttribute($ai);
/* 160 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 163 */           this.$_ngcc_current_state = 1;
/* 164 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
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
/*     */       case 10:
/* 184 */         this.$_ngcc_current_state = 9;
/* 185 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 190 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 195 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 196 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 199 */           this.$_ngcc_current_state = 0;
/* 200 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 206 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 207 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 208 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 211 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 212 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 218 */         if ($__uri.equals("") && $__local.equals("processContents")) {
/* 219 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 222 */           this.$_ngcc_current_state = 1;
/* 223 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 229 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/* 237 */     this.$uri = $__uri;
/* 238 */     this.$localName = $__local;
/* 239 */     this.$qname = $__qname;
/* 240 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 243 */         this.$_ngcc_current_state = 9;
/* 244 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 249 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 254 */         this.$_ngcc_current_state = 0;
/* 255 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 260 */         if ($__uri.equals("") && $__local.equals("processContents")) {
/* 261 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 264 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 270 */         h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 271 */         spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 276 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 277 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 280 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 286 */         this.$_ngcc_current_state = 1;
/* 287 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 292 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 300 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 303 */         this.$_ngcc_current_state = 9;
/* 304 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 309 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 314 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 315 */           this.$runtime.consumeAttribute($ai);
/* 316 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 319 */         this.$_ngcc_current_state = 0;
/* 320 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 326 */         this.ns = $value;
/* 327 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 332 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 333 */           NGCCHandler nGCCHandler = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 334 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 337 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 338 */           NGCCHandler nGCCHandler = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 339 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 342 */         h = new foreignAttributes(this, this._source, this.$runtime, 554, null);
/* 343 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 350 */         this.modeValue = $value;
/* 351 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 356 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 357 */           this.$runtime.consumeAttribute($ai);
/* 358 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 361 */         this.$_ngcc_current_state = 1;
/* 362 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 370 */     switch ($__cookie__) {
/*     */       
/*     */       case 556:
/* 373 */         this.annotation = (AnnotationImpl)$__result__;
/* 374 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 554:
/* 379 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 380 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 387 */     return (this.$_ngcc_current_state == 5 || this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private WildcardImpl makeResult() {
/* 392 */     if (this.modeValue == null) this.modeValue = "strict";
/*     */     
/* 394 */     int mode = -1;
/* 395 */     if (this.modeValue.equals("strict")) mode = 2; 
/* 396 */     if (this.modeValue.equals("lax")) mode = 1; 
/* 397 */     if (this.modeValue.equals("skip")) mode = 3; 
/* 398 */     if (mode == -1) throw new InternalError();
/*     */     
/* 400 */     if (this.ns == null || this.ns.equals("##any")) {
/* 401 */       return (WildcardImpl)new WildcardImpl.Any(this.$runtime.document, this.annotation, this.locator, this.fa, mode);
/*     */     }
/* 403 */     if (this.ns.equals("##other")) {
/* 404 */       return (WildcardImpl)new WildcardImpl.Other(this.$runtime.document, this.annotation, this.locator, this.fa, this.$runtime.currentSchema.getTargetNamespace(), mode);
/*     */     }
/*     */ 
/*     */     
/* 408 */     StringTokenizer tokens = new StringTokenizer(this.ns);
/* 409 */     HashSet<String> s = new HashSet();
/* 410 */     while (tokens.hasMoreTokens()) {
/* 411 */       String ns = tokens.nextToken();
/* 412 */       if (ns.equals("##local")) ns = ""; 
/* 413 */       if (ns.equals("##targetNamespace")) ns = this.$runtime.currentSchema.getTargetNamespace(); 
/* 414 */       s.add(ns);
/*     */     } 
/*     */     
/* 417 */     return (WildcardImpl)new WildcardImpl.Finite(this.$runtime.document, this.annotation, this.locator, this.fa, s, mode);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\wildcardBody.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
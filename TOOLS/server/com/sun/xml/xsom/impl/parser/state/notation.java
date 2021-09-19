/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.NotationImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class notation
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private String pub;
/*     */   private ForeignAttributesImpl fa;
/*     */   private String sys;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  32 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public notation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  36 */     super(source, parent, cookie);
/*  37 */     this.$runtime = runtime;
/*  38 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public notation(NGCCRuntimeEx runtime) {
/*  42 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  51 */     this.$uri = $__uri;
/*  52 */     this.$localName = $__local;
/*  53 */     this.$qname = $__qname;
/*  54 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/*  57 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/*  58 */           this.$runtime.consumeAttribute($ai);
/*  59 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  62 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  68 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  69 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 386, null, AnnotationContext.NOTATION);
/*  70 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  73 */           this.$_ngcc_current_state = 1;
/*  74 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/*  80 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  81 */           this.$runtime.consumeAttribute($ai);
/*  82 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/*  91 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/*  92 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  93 */           action0();
/*  94 */           this.$_ngcc_current_state = 14;
/*     */         } else {
/*     */           
/*  97 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 103 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 108 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 109 */           this.$runtime.consumeAttribute($ai);
/* 110 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 113 */           this.$_ngcc_current_state = 2;
/* 114 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 120 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 121 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 399, null);
/* 122 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 125 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 131 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 139 */     this.$uri = $__uri;
/* 140 */     this.$localName = $__local;
/* 141 */     this.$qname = $__qname;
/* 142 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 145 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 146 */           this.$runtime.consumeAttribute($ai);
/* 147 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 150 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 156 */         this.$_ngcc_current_state = 1;
/* 157 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 162 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 163 */           this.$runtime.consumeAttribute($ai);
/* 164 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 167 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 173 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 178 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 179 */           this.$runtime.consumeAttribute($ai);
/* 180 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 183 */           this.$_ngcc_current_state = 2;
/* 184 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 190 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 191 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 399, null);
/* 192 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 195 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 201 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 202 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 203 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 206 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 212 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 220 */     this.$uri = $__uri;
/* 221 */     this.$localName = $__local;
/* 222 */     this.$qname = $__qname;
/* 223 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 226 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 227 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 230 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 236 */         this.$_ngcc_current_state = 1;
/* 237 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 242 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 243 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 246 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 252 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 257 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 258 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 261 */           this.$_ngcc_current_state = 2;
/* 262 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 268 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 269 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 399, null);
/* 270 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 273 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 279 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 287 */     this.$uri = $__uri;
/* 288 */     this.$localName = $__local;
/* 289 */     this.$qname = $__qname;
/* 290 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 293 */         this.$_ngcc_current_state = 1;
/* 294 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 299 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 300 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 303 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 309 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 310 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 313 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 319 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 320 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 323 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 329 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 334 */         this.$_ngcc_current_state = 2;
/* 335 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 340 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 348 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 351 */         this.pub = $value;
/* 352 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 357 */         this.sys = $value;
/* 358 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 363 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 364 */           this.$runtime.consumeAttribute($ai);
/* 365 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 371 */         this.name = $value;
/* 372 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 377 */         this.$_ngcc_current_state = 1;
/* 378 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 383 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 384 */           this.$runtime.consumeAttribute($ai);
/* 385 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 391 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 396 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 397 */           this.$runtime.consumeAttribute($ai);
/* 398 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 401 */         this.$_ngcc_current_state = 2;
/* 402 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 14:
/* 408 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 409 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 399, null);
/* 410 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 418 */     switch ($__cookie__) {
/*     */       
/*     */       case 386:
/* 421 */         this.ann = (AnnotationImpl)$__result__;
/* 422 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 399:
/* 427 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 428 */         this.$_ngcc_current_state = 13;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 435 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private XSNotation makeResult() {
/* 441 */     return (XSNotation)new NotationImpl(this.$runtime.document, this.ann, this.loc, this.fa, this.name, this.pub, this.sys);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\notation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttributeDeclImpl;
/*     */ import com.sun.xml.xsom.impl.AttributeUseImpl;
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class attributeUses
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String use;
/*     */   private AttributesHolder owner;
/*     */   private ForeignAttributesImpl fa;
/*     */   private WildcardImpl wildcard;
/*     */   private AnnotationImpl annotation;
/*     */   private UName attDeclName;
/*     */   private AttributeDeclImpl anonymousDecl;
/*     */   private String defaultValue;
/*     */   private String fixedValue;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  37 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private UName groupName; protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private Ref.Attribute decl; private Locator wloc; private Locator locator;
/*     */   public attributeUses(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  41 */     super(source, parent, cookie);
/*  42 */     this.$runtime = runtime;
/*  43 */     this.owner = _owner;
/*  44 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */   
/*     */   public attributeUses(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  48 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  53 */     this.owner.setWildcard(this.wildcard);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  58 */     this.wloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  63 */     this.owner.addAttGroup((Ref.AttGroup)new DelayedRef.AttGroup((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.groupName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  69 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/*  74 */     if ("prohibited".equals(this.use)) {
/*  75 */       this.owner.addProhibitedAttribute(this.attDeclName);
/*     */     } else {
/*  77 */       this.owner.addAttributeUse(this.attDeclName, new AttributeUseImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.decl, this.$runtime.createXmlString(this.defaultValue), this.$runtime.createXmlString(this.fixedValue), "required".equals(this.use)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action5() throws SAXException {
/*  87 */     this.decl = (Ref.Attribute)new DelayedRef.Attribute((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.attDeclName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action6() throws SAXException {
/*  94 */     this.decl = (Ref.Attribute)this.anonymousDecl;
/*  95 */     this.attDeclName = new UName(this.anonymousDecl.getTargetNamespace(), this.anonymousDecl.getName());
/*     */ 
/*     */     
/*  98 */     this.defaultValue = null;
/*  99 */     this.fixedValue = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action7() throws SAXException {
/* 105 */     this.locator = this.$runtime.copyLocator();
/* 106 */     this.use = null;
/* 107 */     this.defaultValue = null;
/* 108 */     this.fixedValue = null;
/* 109 */     this.decl = null;
/* 110 */     this.annotation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 116 */     this.$uri = $__uri;
/* 117 */     this.$localName = $__local;
/* 118 */     this.$qname = $__qname;
/* 119 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 122 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 123 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 124 */           action7();
/* 125 */           this.$_ngcc_current_state = 33;
/*     */         
/*     */         }
/* 128 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 129 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 130 */           action3();
/* 131 */           this.$_ngcc_current_state = 13;
/*     */         
/*     */         }
/* 134 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 135 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 136 */           action1();
/* 137 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 140 */           this.$_ngcc_current_state = 0;
/* 141 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 13:
/* 149 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 150 */           this.$runtime.consumeAttribute($ai);
/* 151 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 154 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 160 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 161 */           this.$runtime.consumeAttribute($ai);
/* 162 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 165 */           this.$_ngcc_current_state = 17;
/* 166 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 172 */         action2();
/* 173 */         this.$_ngcc_current_state = 7;
/* 174 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 179 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 180 */           this.$runtime.consumeAttribute($ai);
/* 181 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 184 */           this.$_ngcc_current_state = 29;
/* 185 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 191 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 192 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 409, null, AnnotationContext.ATTRIBUTE_USE);
/* 193 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 196 */           this.$_ngcc_current_state = 8;
/* 197 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 203 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 204 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 402, this.wloc);
/* 205 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 208 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 214 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 215 */           this.$runtime.consumeAttribute($ai);
/* 216 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 219 */           this.$_ngcc_current_state = 25;
/* 220 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 226 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 231 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 232 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 233 */           action7();
/* 234 */           this.$_ngcc_current_state = 33;
/*     */         
/*     */         }
/* 237 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 238 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 239 */           action3();
/* 240 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 243 */           this.$_ngcc_current_state = 1;
/* 244 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 19:
/* 251 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 252 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 420, null, AnnotationContext.ATTRIBUTE_USE);
/* 253 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 256 */           this.$_ngcc_current_state = 18;
/* 257 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 263 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 264 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 427, this.locator, true, this.defaultValue, this.fixedValue);
/* 265 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 268 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 269 */           this.$runtime.consumeAttribute($ai);
/* 270 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 273 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/* 280 */         action4();
/* 281 */         this.$_ngcc_current_state = 15;
/* 282 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 287 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 295 */     this.$uri = $__uri;
/* 296 */     this.$localName = $__local;
/* 297 */     this.$qname = $__qname;
/* 298 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 301 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 302 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 303 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 306 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 312 */         this.$_ngcc_current_state = 0;
/* 313 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 318 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 319 */           this.$runtime.consumeAttribute($ai);
/* 320 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 323 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 329 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 330 */           this.$runtime.consumeAttribute($ai);
/* 331 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 334 */           this.$_ngcc_current_state = 17;
/* 335 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 341 */         action2();
/* 342 */         this.$_ngcc_current_state = 7;
/* 343 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 348 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 349 */           this.$runtime.consumeAttribute($ai);
/* 350 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 353 */           this.$_ngcc_current_state = 29;
/* 354 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 360 */         this.$_ngcc_current_state = 8;
/* 361 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 366 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 367 */           this.$runtime.consumeAttribute($ai);
/* 368 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 371 */           this.$_ngcc_current_state = 25;
/* 372 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 378 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/* 379 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 402, this.wloc);
/* 380 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 383 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 389 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 390 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 391 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 394 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 400 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 405 */         this.$_ngcc_current_state = 1;
/* 406 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 411 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 412 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 418, null);
/* 413 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 416 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 422 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 423 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 424 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 427 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 433 */         if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/* 434 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 427, this.locator, true, this.defaultValue, this.fixedValue);
/* 435 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 438 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 439 */           this.$runtime.consumeAttribute($ai);
/* 440 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 443 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 19:
/* 450 */         this.$_ngcc_current_state = 18;
/* 451 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 456 */         action4();
/* 457 */         this.$_ngcc_current_state = 15;
/* 458 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 463 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 471 */     this.$uri = $__uri;
/* 472 */     this.$localName = $__local;
/* 473 */     this.$qname = $__qname;
/* 474 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 477 */         this.$_ngcc_current_state = 0;
/* 478 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 483 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 484 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 487 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 493 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 494 */           this.$_ngcc_current_state = 27;
/*     */         } else {
/*     */           
/* 497 */           this.$_ngcc_current_state = 17;
/* 498 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 504 */         action2();
/* 505 */         this.$_ngcc_current_state = 7;
/* 506 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 511 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 512 */           this.$_ngcc_current_state = 35;
/*     */         } else {
/*     */           
/* 515 */           this.$_ngcc_current_state = 29;
/* 516 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 522 */         this.$_ngcc_current_state = 8;
/* 523 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 528 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 529 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 402, this.wloc);
/* 530 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 533 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 539 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 540 */           this.$_ngcc_current_state = 31;
/*     */         } else {
/*     */           
/* 543 */           this.$_ngcc_current_state = 25;
/* 544 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 550 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 555 */         this.$_ngcc_current_state = 1;
/* 556 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 561 */         this.$_ngcc_current_state = 18;
/* 562 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 567 */         if (($__uri.equals("") && $__local.equals("name")) || ($__uri.equals("") && $__local.equals("form"))) {
/* 568 */           NGCCHandler h = new attributeDeclBody(this, this._source, this.$runtime, 427, this.locator, true, this.defaultValue, this.fixedValue);
/* 569 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 572 */         else if ($__uri.equals("") && $__local.equals("ref")) {
/* 573 */           this.$_ngcc_current_state = 22;
/*     */         } else {
/*     */           
/* 576 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/* 583 */         action4();
/* 584 */         this.$_ngcc_current_state = 15;
/* 585 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 590 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 598 */     this.$uri = $__uri;
/* 599 */     this.$localName = $__local;
/* 600 */     this.$qname = $__qname;
/* 601 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 604 */         this.$_ngcc_current_state = 0;
/* 605 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 610 */         this.$_ngcc_current_state = 17;
/* 611 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 616 */         action2();
/* 617 */         this.$_ngcc_current_state = 7;
/* 618 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 623 */         this.$_ngcc_current_state = 29;
/* 624 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 629 */         this.$_ngcc_current_state = 8;
/* 630 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 635 */         this.$_ngcc_current_state = 25;
/* 636 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 34:
/* 641 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 642 */           this.$_ngcc_current_state = 29;
/*     */         } else {
/*     */           
/* 645 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 651 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 656 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 657 */           this.$_ngcc_current_state = 25;
/*     */         } else {
/*     */           
/* 660 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 666 */         this.$_ngcc_current_state = 1;
/* 667 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 672 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 673 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 676 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 682 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 683 */           this.$_ngcc_current_state = 19;
/*     */         } else {
/*     */           
/* 686 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 692 */         this.$_ngcc_current_state = 18;
/* 693 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 698 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 699 */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           
/* 702 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 708 */         action4();
/* 709 */         this.$_ngcc_current_state = 15;
/* 710 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 715 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 723 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 726 */         h = new qname(this, this._source, this.$runtime, 412);
/* 727 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 732 */         this.$_ngcc_current_state = 0;
/* 733 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 738 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 739 */           this.$runtime.consumeAttribute($ai);
/* 740 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 27:
/* 746 */         this.fixedValue = $value;
/* 747 */         this.$_ngcc_current_state = 26;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 25:
/* 752 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 753 */           this.$runtime.consumeAttribute($ai);
/* 754 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 757 */         this.$_ngcc_current_state = 17;
/* 758 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 764 */         action2();
/* 765 */         this.$_ngcc_current_state = 7;
/* 766 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 33:
/* 771 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 772 */           this.$runtime.consumeAttribute($ai);
/* 773 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 776 */         this.$_ngcc_current_state = 29;
/* 777 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 9:
/* 783 */         this.$_ngcc_current_state = 8;
/* 784 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 789 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 790 */           h = new wildcardBody(this, this._source, this.$runtime, 402, this.wloc);
/* 791 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 794 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 795 */           h = new wildcardBody(this, this._source, this.$runtime, 402, this.wloc);
/* 796 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 29:
/* 803 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 804 */           this.$runtime.consumeAttribute($ai);
/* 805 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 808 */         this.$_ngcc_current_state = 25;
/* 809 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 815 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 820 */         this.$_ngcc_current_state = 1;
/* 821 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 22:
/* 826 */         h = new qname(this, this._source, this.$runtime, 423);
/* 827 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 17:
/* 832 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 833 */           h = new attributeDeclBody(this, this._source, this.$runtime, 427, this.locator, true, this.defaultValue, this.fixedValue);
/* 834 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 837 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 838 */           h = new attributeDeclBody(this, this._source, this.$runtime, 427, this.locator, true, this.defaultValue, this.fixedValue);
/* 839 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 842 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 843 */           this.$runtime.consumeAttribute($ai);
/* 844 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 19:
/* 852 */         this.$_ngcc_current_state = 18;
/* 853 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 31:
/* 858 */         this.defaultValue = $value;
/* 859 */         this.$_ngcc_current_state = 30;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 16:
/* 864 */         action4();
/* 865 */         this.$_ngcc_current_state = 15;
/* 866 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 35:
/* 871 */         this.use = $value;
/* 872 */         this.$_ngcc_current_state = 34;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 879 */     switch ($__cookie__) {
/*     */       
/*     */       case 412:
/* 882 */         this.groupName = (UName)$__result__;
/* 883 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 402:
/* 888 */         this.wildcard = (WildcardImpl)$__result__;
/* 889 */         action0();
/* 890 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 418:
/* 895 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 896 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 420:
/* 901 */         this.annotation = (AnnotationImpl)$__result__;
/* 902 */         this.$_ngcc_current_state = 18;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 409:
/* 907 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 423:
/* 912 */         this.attDeclName = (UName)$__result__;
/* 913 */         action5();
/* 914 */         this.$_ngcc_current_state = 21;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 427:
/* 919 */         this.anonymousDecl = (AttributeDeclImpl)$__result__;
/* 920 */         action6();
/* 921 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 928 */     return (this.$_ngcc_current_state == 5 || this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\attributeUses.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
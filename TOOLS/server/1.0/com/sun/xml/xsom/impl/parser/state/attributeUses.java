/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttributeDeclImpl;
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.attributeDeclBody;
/*     */ import com.sun.xml.xsom.impl.parser.state.qname;
/*     */ import com.sun.xml.xsom.impl.parser.state.wildcardBody;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class attributeUses extends NGCCHandler {
/*     */   private String use;
/*     */   private AttributesHolder owner;
/*     */   private WildcardImpl wildcard;
/*     */   private AnnotationImpl annotation;
/*     */   private UName attDeclName;
/*     */   private AttributeDeclImpl anonymousDecl;
/*     */   private String defaultValue;
/*     */   private String fixedValue;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  35 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private UName groupName; protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private Ref.Attribute decl; private Locator wloc; private Locator locator;
/*     */   public attributeUses(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AttributesHolder _owner) {
/*  39 */     super(source, parent, cookie);
/*  40 */     this.$runtime = runtime;
/*  41 */     this.owner = _owner;
/*  42 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */   
/*     */   public attributeUses(NGCCRuntimeEx runtime, AttributesHolder _owner) {
/*  46 */     this(null, (NGCCEventSource)runtime, runtime, -1, _owner);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  51 */     this.owner.setWildcard(this.wildcard);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  56 */     this.wloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  61 */     this.owner.addAttGroup((Ref.AttGroup)new DelayedRef.AttGroup((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.groupName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  67 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/*  72 */     if ("prohibited".equals(this.use)) {
/*  73 */       this.owner.addProhibitedAttribute(this.attDeclName);
/*     */     } else {
/*  75 */       this.owner.addAttributeUse(this.attDeclName, new AttributeUseImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.decl, this.defaultValue, this.fixedValue, this.$runtime.createValidationContext(), "required".equals(this.use)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action5() throws SAXException {
/*  84 */     this.decl = (Ref.Attribute)new DelayedRef.Attribute((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.attDeclName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action6() throws SAXException {
/*  91 */     this.decl = (Ref.Attribute)this.anonymousDecl;
/*  92 */     this.attDeclName = new UName(this.anonymousDecl.getTargetNamespace(), this.anonymousDecl.getName());
/*     */ 
/*     */     
/*  95 */     this.defaultValue = null;
/*  96 */     this.fixedValue = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action7() throws SAXException {
/* 102 */     this.locator = this.$runtime.copyLocator();
/* 103 */     this.use = null;
/* 104 */     this.defaultValue = null;
/* 105 */     this.fixedValue = null;
/* 106 */     this.decl = null;
/* 107 */     this.annotation = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 113 */     this.$uri = $__uri;
/* 114 */     this.$localName = $__local;
/* 115 */     this.$qname = $__qname;
/* 116 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 119 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 120 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 507, this.wloc);
/* 121 */           spawnChildFromEnterElement((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 124 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 130 */         action4();
/* 131 */         this.$_ngcc_current_state = 15;
/* 132 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 137 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 138 */           annotation annotation = new annotation(this, this._source, this.$runtime, 524, null, AnnotationContext.ATTRIBUTE_USE);
/* 139 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 142 */           this.$_ngcc_current_state = 16;
/* 143 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 24:
/* 149 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 150 */           this.$runtime.consumeAttribute($ai);
/* 151 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 154 */           this.$_ngcc_current_state = 17;
/* 155 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 161 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 162 */           annotation annotation = new annotation(this, this._source, this.$runtime, 514, null, AnnotationContext.ATTRIBUTE_USE);
/* 163 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 166 */           this.$_ngcc_current_state = 8;
/* 167 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 173 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 174 */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 531, this.locator, true, this.defaultValue, this.fixedValue);
/* 175 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 178 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 179 */           this.$runtime.consumeAttribute($ai);
/* 180 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 183 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 32:
/* 190 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 191 */           this.$runtime.consumeAttribute($ai);
/* 192 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 195 */           this.$_ngcc_current_state = 28;
/* 196 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 202 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 207 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 208 */           this.$runtime.consumeAttribute($ai);
/* 209 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 212 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 218 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 219 */           this.$runtime.consumeAttribute($ai);
/* 220 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 223 */           this.$_ngcc_current_state = 24;
/* 224 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 230 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 231 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 232 */           action7();
/* 233 */           this.$_ngcc_current_state = 32;
/*     */         
/*     */         }
/* 236 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 237 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 238 */           action3();
/* 239 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 242 */           this.$_ngcc_current_state = 1;
/* 243 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 250 */         action2();
/* 251 */         this.$_ngcc_current_state = 7;
/* 252 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 257 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 258 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 259 */           action7();
/* 260 */           this.$_ngcc_current_state = 32;
/*     */         
/*     */         }
/* 263 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 264 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 265 */           action3();
/* 266 */           this.$_ngcc_current_state = 13;
/*     */         
/*     */         }
/* 269 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 270 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 271 */           action1();
/* 272 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 275 */           this.$_ngcc_current_state = 0;
/* 276 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 284 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 292 */     this.$uri = $__uri;
/* 293 */     this.$localName = $__local;
/* 294 */     this.$qname = $__qname;
/* 295 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 298 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/* 299 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 507, this.wloc);
/* 300 */           spawnChildFromLeaveElement((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 303 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 309 */         action4();
/* 310 */         this.$_ngcc_current_state = 15;
/* 311 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 316 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 317 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 318 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 321 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 327 */         this.$_ngcc_current_state = 16;
/* 328 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 24:
/* 333 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 334 */           this.$runtime.consumeAttribute($ai);
/* 335 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 338 */           this.$_ngcc_current_state = 17;
/* 339 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 345 */         this.$_ngcc_current_state = 8;
/* 346 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 351 */         if ((($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/* 352 */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 531, this.locator, true, this.defaultValue, this.fixedValue);
/* 353 */           spawnChildFromLeaveElement((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 356 */         else if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 357 */           this.$runtime.consumeAttribute($ai);
/* 358 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 361 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 32:
/* 368 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 369 */           this.$runtime.consumeAttribute($ai);
/* 370 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 373 */           this.$_ngcc_current_state = 28;
/* 374 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 380 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 385 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 386 */           this.$runtime.consumeAttribute($ai);
/* 387 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 390 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 396 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 397 */           this.$runtime.consumeAttribute($ai);
/* 398 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 401 */           this.$_ngcc_current_state = 24;
/* 402 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 408 */         this.$_ngcc_current_state = 1;
/* 409 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 414 */         this.$_ngcc_current_state = 0;
/* 415 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 420 */         action2();
/* 421 */         this.$_ngcc_current_state = 7;
/* 422 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 427 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) {
/* 428 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 429 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 432 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 438 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) {
/* 439 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 440 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 443 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 449 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 457 */     this.$uri = $__uri;
/* 458 */     this.$localName = $__local;
/* 459 */     this.$qname = $__qname;
/* 460 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 463 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 464 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 507, this.wloc);
/* 465 */           spawnChildFromEnterAttribute((NGCCEventReceiver)wildcardBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 468 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 474 */         action4();
/* 475 */         this.$_ngcc_current_state = 15;
/* 476 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 481 */         this.$_ngcc_current_state = 16;
/* 482 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 24:
/* 487 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 488 */           this.$_ngcc_current_state = 26;
/*     */         } else {
/*     */           
/* 491 */           this.$_ngcc_current_state = 17;
/* 492 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 498 */         this.$_ngcc_current_state = 8;
/* 499 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 17:
/* 504 */         if (($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("name"))) {
/* 505 */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 531, this.locator, true, this.defaultValue, this.fixedValue);
/* 506 */           spawnChildFromEnterAttribute((NGCCEventReceiver)attributeDeclBody, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 509 */         else if ($__uri.equals("") && $__local.equals("ref")) {
/* 510 */           this.$_ngcc_current_state = 21;
/*     */         } else {
/*     */           
/* 513 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 32:
/* 520 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 521 */           this.$_ngcc_current_state = 34;
/*     */         } else {
/*     */           
/* 524 */           this.$_ngcc_current_state = 28;
/* 525 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 531 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 536 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 537 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 540 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 546 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 547 */           this.$_ngcc_current_state = 30;
/*     */         } else {
/*     */           
/* 550 */           this.$_ngcc_current_state = 24;
/* 551 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 557 */         this.$_ngcc_current_state = 1;
/* 558 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 563 */         action2();
/* 564 */         this.$_ngcc_current_state = 7;
/* 565 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 570 */         this.$_ngcc_current_state = 0;
/* 571 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 576 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 584 */     this.$uri = $__uri;
/* 585 */     this.$localName = $__local;
/* 586 */     this.$qname = $__qname;
/* 587 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 20:
/* 590 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 591 */           this.$_ngcc_current_state = 18;
/*     */         } else {
/*     */           
/* 594 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 600 */         action4();
/* 601 */         this.$_ngcc_current_state = 15;
/* 602 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 607 */         this.$_ngcc_current_state = 16;
/* 608 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 24:
/* 613 */         this.$_ngcc_current_state = 17;
/* 614 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 619 */         this.$_ngcc_current_state = 8;
/* 620 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 625 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 626 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 629 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 33:
/* 635 */         if ($__uri.equals("") && $__local.equals("use")) {
/* 636 */           this.$_ngcc_current_state = 28;
/*     */         } else {
/*     */           
/* 639 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 32:
/* 645 */         this.$_ngcc_current_state = 28;
/* 646 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 651 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 656 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 657 */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           
/* 660 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 666 */         this.$_ngcc_current_state = 24;
/* 667 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 672 */         if ($__uri.equals("") && $__local.equals("default")) {
/* 673 */           this.$_ngcc_current_state = 24;
/*     */         } else {
/*     */           
/* 676 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 682 */         this.$_ngcc_current_state = 1;
/* 683 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 688 */         this.$_ngcc_current_state = 0;
/* 689 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 694 */         action2();
/* 695 */         this.$_ngcc_current_state = 7;
/* 696 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 701 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     qname qname2;
/*     */     qname qname1;
/* 709 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 712 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 713 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 507, this.wloc);
/* 714 */           spawnChildFromText((NGCCEventReceiver)wildcardBody, $value);
/*     */           break;
/*     */         } 
/* 717 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 718 */           wildcardBody wildcardBody = new wildcardBody(this, this._source, this.$runtime, 507, this.wloc);
/* 719 */           spawnChildFromText((NGCCEventReceiver)wildcardBody, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/* 726 */         action4();
/* 727 */         this.$_ngcc_current_state = 15;
/* 728 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 733 */         qname2 = new qname(this, this._source, this.$runtime, 517);
/* 734 */         spawnChildFromText((NGCCEventReceiver)qname2, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 24:
/* 739 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 740 */           this.$runtime.consumeAttribute($ai);
/* 741 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 744 */         this.$_ngcc_current_state = 17;
/* 745 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 18:
/* 751 */         this.$_ngcc_current_state = 16;
/* 752 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 26:
/* 757 */         this.fixedValue = $value;
/* 758 */         this.$_ngcc_current_state = 25;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 763 */         this.$_ngcc_current_state = 8;
/* 764 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 21:
/* 769 */         qname1 = new qname(this, this._source, this.$runtime, 527);
/* 770 */         spawnChildFromText((NGCCEventReceiver)qname1, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 17:
/* 775 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 776 */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 531, this.locator, true, this.defaultValue, this.fixedValue);
/* 777 */           spawnChildFromText((NGCCEventReceiver)attributeDeclBody, $value);
/*     */           break;
/*     */         } 
/* 780 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 781 */           attributeDeclBody attributeDeclBody = new attributeDeclBody(this, this._source, this.$runtime, 531, this.locator, true, this.defaultValue, this.fixedValue);
/* 782 */           spawnChildFromText((NGCCEventReceiver)attributeDeclBody, $value);
/*     */           break;
/*     */         } 
/* 785 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 786 */           this.$runtime.consumeAttribute($ai);
/* 787 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 34:
/* 795 */         this.use = $value;
/* 796 */         this.$_ngcc_current_state = 33;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 30:
/* 801 */         this.defaultValue = $value;
/* 802 */         this.$_ngcc_current_state = 29;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 32:
/* 807 */         if (($ai = this.$runtime.getAttributeIndex("", "use")) >= 0) {
/* 808 */           this.$runtime.consumeAttribute($ai);
/* 809 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 812 */         this.$_ngcc_current_state = 28;
/* 813 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 819 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 824 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 825 */           this.$runtime.consumeAttribute($ai);
/* 826 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 28:
/* 832 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 833 */           this.$runtime.consumeAttribute($ai);
/* 834 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 837 */         this.$_ngcc_current_state = 24;
/* 838 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 5:
/* 844 */         this.$_ngcc_current_state = 1;
/* 845 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 850 */         this.$_ngcc_current_state = 0;
/* 851 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 856 */         action2();
/* 857 */         this.$_ngcc_current_state = 7;
/* 858 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 865 */     switch ($__cookie__) {
/*     */       
/*     */       case 507:
/* 868 */         this.wildcard = (WildcardImpl)$__result__;
/* 869 */         action0();
/* 870 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 517:
/* 875 */         this.groupName = (UName)$__result__;
/* 876 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 514:
/* 881 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 527:
/* 886 */         this.attDeclName = (UName)$__result__;
/* 887 */         action5();
/* 888 */         this.$_ngcc_current_state = 20;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 531:
/* 893 */         this.anonymousDecl = (AttributeDeclImpl)$__result__;
/* 894 */         action6();
/* 895 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 524:
/* 900 */         this.annotation = (AnnotationImpl)$__result__;
/* 901 */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 908 */     return (this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 5 || this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\attributeUses.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
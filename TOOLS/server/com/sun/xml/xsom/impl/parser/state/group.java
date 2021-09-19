/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class group
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ModelGroupImpl term;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private ModelGroupDeclImpl result;
/*     */   private Locator loc;
/*     */   private Locator mloc;
/*     */   private String compositorName;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public group(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public group(NGCCRuntimeEx runtime) {
/*  41 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     this.result = new ModelGroupDeclImpl(this.$runtime.document, this.annotation, this.loc, this.fa, this.$runtime.currentSchema.getTargetNamespace(), this.name, this.term);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  57 */     this.mloc = this.$runtime.copyLocator();
/*  58 */     this.compositorName = this.$localName;
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  63 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  68 */     this.$uri = $__uri;
/*  69 */     this.$localName = $__local;
/*  70 */     this.$qname = $__qname;
/*  71 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 11:
/*  74 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/*  75 */           this.$runtime.consumeAttribute($ai);
/*  76 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  79 */           this.$_ngcc_current_state = 10;
/*  80 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  86 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/*  87 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 315, null);
/*  88 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  91 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  97 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  98 */           this.$runtime.consumeAttribute($ai);
/*  99 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 102 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 108 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 109 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 110 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 113 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 119 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/* 120 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 313, this.mloc, this.compositorName);
/* 121 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 124 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 130 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 135 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 136 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 137 */           action2();
/* 138 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 141 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 147 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 148 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 317, null, AnnotationContext.MODELGROUP_DECL);
/* 149 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 152 */           this.$_ngcc_current_state = 5;
/* 153 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 159 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 167 */     this.$uri = $__uri;
/* 168 */     this.$localName = $__local;
/* 169 */     this.$qname = $__qname;
/* 170 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 173 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 174 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 175 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 178 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 184 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 185 */           this.$runtime.consumeAttribute($ai);
/* 186 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 189 */           this.$_ngcc_current_state = 10;
/* 190 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 196 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 197 */           this.$runtime.consumeAttribute($ai);
/* 198 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 201 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 207 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 208 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 313, this.mloc, this.compositorName);
/* 209 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 212 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 218 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 219 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 220 */           this.$_ngcc_current_state = 0;
/* 221 */           action0();
/*     */         } else {
/*     */           
/* 224 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 230 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 235 */         this.$_ngcc_current_state = 5;
/* 236 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 241 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 249 */     this.$uri = $__uri;
/* 250 */     this.$localName = $__local;
/* 251 */     this.$qname = $__qname;
/* 252 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 11:
/* 255 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 256 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 259 */           this.$_ngcc_current_state = 10;
/* 260 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 266 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 267 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 270 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 276 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 281 */         this.$_ngcc_current_state = 5;
/* 282 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 287 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 295 */     this.$uri = $__uri;
/* 296 */     this.$localName = $__local;
/* 297 */     this.$qname = $__qname;
/* 298 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 301 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 302 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 305 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 311 */         this.$_ngcc_current_state = 10;
/* 312 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 317 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 318 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 321 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 327 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 332 */         this.$_ngcc_current_state = 5;
/* 333 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 338 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 346 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 11:
/* 349 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 350 */           this.$runtime.consumeAttribute($ai);
/* 351 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 354 */         this.$_ngcc_current_state = 10;
/* 355 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 10:
/* 361 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 362 */           this.$runtime.consumeAttribute($ai);
/* 363 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 369 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 374 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 379 */         this.name = $value;
/* 380 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 385 */         this.$_ngcc_current_state = 5;
/* 386 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 393 */     switch ($__cookie__) {
/*     */       
/*     */       case 313:
/* 396 */         this.term = (ModelGroupImpl)$__result__;
/* 397 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 317:
/* 402 */         this.annotation = (AnnotationImpl)$__result__;
/* 403 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 315:
/* 408 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 409 */         action1();
/* 410 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 417 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\group.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ class attributeGroupDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private AttGroupDeclImpl result;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public attributeGroupDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.$_ngcc_current_state = 14;
/*     */   }
/*     */   
/*     */   public attributeGroupDecl(NGCCRuntimeEx runtime) {
/*  40 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  45 */     this.result = new AttGroupDeclImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  51 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  56 */     this.$uri = $__uri;
/*  57 */     this.$localName = $__local;
/*  58 */     this.$qname = $__qname;
/*  59 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/*  62 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  63 */           this.$runtime.consumeAttribute($ai);
/*  64 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  67 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/*  73 */         action0();
/*  74 */         this.$_ngcc_current_state = 2;
/*  75 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  80 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  81 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 674, this.fa);
/*  82 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/*  91 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*  92 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  93 */           action1();
/*  94 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/*  97 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 103 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/* 104 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 669, (AttributesHolder)this.result);
/* 105 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 108 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 114 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 119 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 120 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 672, null, AnnotationContext.ATTRIBUTE_GROUP);
/* 121 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 124 */           this.$_ngcc_current_state = 3;
/* 125 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 131 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 132 */           this.$runtime.consumeAttribute($ai);
/* 133 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 136 */           this.$_ngcc_current_state = 6;
/* 137 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 143 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 151 */     this.$uri = $__uri;
/* 152 */     this.$localName = $__local;
/* 153 */     this.$qname = $__qname;
/* 154 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 157 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 158 */           this.$runtime.consumeAttribute($ai);
/* 159 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 162 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 168 */         action0();
/* 169 */         this.$_ngcc_current_state = 2;
/* 170 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 175 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 176 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 674, this.fa);
/* 177 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 180 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 186 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 187 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 669, (AttributesHolder)this.result);
/* 188 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 191 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 197 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 198 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 199 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 202 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 208 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 213 */         this.$_ngcc_current_state = 3;
/* 214 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 219 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 220 */           this.$runtime.consumeAttribute($ai);
/* 221 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 224 */           this.$_ngcc_current_state = 6;
/* 225 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 231 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 239 */     this.$uri = $__uri;
/* 240 */     this.$localName = $__local;
/* 241 */     this.$qname = $__qname;
/* 242 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 245 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 246 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 249 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 255 */         action0();
/* 256 */         this.$_ngcc_current_state = 2;
/* 257 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 262 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 267 */         this.$_ngcc_current_state = 3;
/* 268 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 273 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 274 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 277 */           this.$_ngcc_current_state = 6;
/* 278 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 284 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 292 */     this.$uri = $__uri;
/* 293 */     this.$localName = $__local;
/* 294 */     this.$qname = $__qname;
/* 295 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 298 */         action0();
/* 299 */         this.$_ngcc_current_state = 2;
/* 300 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 305 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 306 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 309 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 315 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 316 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 319 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 325 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 330 */         this.$_ngcc_current_state = 3;
/* 331 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 336 */         this.$_ngcc_current_state = 6;
/* 337 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 342 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 350 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 13:
/* 353 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 354 */           this.$runtime.consumeAttribute($ai);
/* 355 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 361 */         this.name = $value;
/* 362 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 367 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 372 */         action0();
/* 373 */         this.$_ngcc_current_state = 2;
/* 374 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 379 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 384 */         this.$_ngcc_current_state = 3;
/* 385 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 390 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 391 */           this.$runtime.consumeAttribute($ai);
/* 392 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 395 */         this.$_ngcc_current_state = 6;
/* 396 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 404 */     switch ($__cookie__) {
/*     */       
/*     */       case 669:
/* 407 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 672:
/* 412 */         this.annotation = (AnnotationImpl)$__result__;
/* 413 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 674:
/* 418 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 419 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 426 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\attributeGroupDecl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
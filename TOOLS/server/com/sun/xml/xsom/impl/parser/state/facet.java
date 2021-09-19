/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.FacetImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class facet
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String fixed;
/*     */   private String value;
/*     */   private ForeignAttributesImpl fa;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private FacetImpl result;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public facet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public facet(NGCCRuntimeEx runtime) {
/*  41 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     this.result = new FacetImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.$localName, this.$runtime.createXmlString(this.value), this.$runtime.parseBoolean(this.fixed));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  53 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  58 */     this.$uri = $__uri;
/*  59 */     this.$localName = $__local;
/*  60 */     this.$qname = $__qname;
/*  61 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/*  64 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  65 */           this.$runtime.consumeAttribute($ai);
/*  66 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  69 */           this.$_ngcc_current_state = 4;
/*  70 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/*  76 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/*  77 */           this.$runtime.consumeAttribute($ai);
/*  78 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  81 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  87 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  88 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 562, this.fa);
/*  89 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  92 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/*  98 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  99 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 100 */           action1();
/* 101 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 104 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 110 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 115 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 116 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 560, null, AnnotationContext.SIMPLETYPE_DECL);
/* 117 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 120 */           this.$_ngcc_current_state = 1;
/* 121 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 135 */     this.$uri = $__uri;
/* 136 */     this.$localName = $__local;
/* 137 */     this.$qname = $__qname;
/* 138 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 141 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 142 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 143 */           this.$_ngcc_current_state = 0;
/* 144 */           action0();
/*     */         } else {
/*     */           
/* 147 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 153 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 154 */           this.$runtime.consumeAttribute($ai);
/* 155 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 158 */           this.$_ngcc_current_state = 4;
/* 159 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 165 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 166 */           this.$runtime.consumeAttribute($ai);
/* 167 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 170 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 176 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 177 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 562, this.fa);
/* 178 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 181 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 187 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 192 */         this.$_ngcc_current_state = 1;
/* 193 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 206 */     this.$uri = $__uri;
/* 207 */     this.$localName = $__local;
/* 208 */     this.$qname = $__qname;
/* 209 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 212 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 213 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 216 */           this.$_ngcc_current_state = 4;
/* 217 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 223 */         if ($__uri.equals("") && $__local.equals("value")) {
/* 224 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 227 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 233 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 238 */         this.$_ngcc_current_state = 1;
/* 239 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 244 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 252 */     this.$uri = $__uri;
/* 253 */     this.$localName = $__local;
/* 254 */     this.$qname = $__qname;
/* 255 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 258 */         if ($__uri.equals("") && $__local.equals("value")) {
/* 259 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 262 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 268 */         this.$_ngcc_current_state = 4;
/* 269 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 274 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 275 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 278 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 284 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 289 */         this.$_ngcc_current_state = 1;
/* 290 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 295 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 303 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 306 */         this.value = $value;
/* 307 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 312 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 313 */           this.$runtime.consumeAttribute($ai);
/* 314 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 317 */         this.$_ngcc_current_state = 4;
/* 318 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 324 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 325 */           this.$runtime.consumeAttribute($ai);
/* 326 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 332 */         this.fixed = $value;
/* 333 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 338 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 343 */         this.$_ngcc_current_state = 1;
/* 344 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 351 */     switch ($__cookie__) {
/*     */       
/*     */       case 560:
/* 354 */         this.annotation = (AnnotationImpl)$__result__;
/* 355 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 562:
/* 360 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 361 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 368 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\facet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
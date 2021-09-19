/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.Set;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SimpleType_Restriction
/*     */   extends NGCCHandler
/*     */ {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName baseTypeName;
/*     */   private Set finalSet;
/*     */   private ForeignAttributesImpl fa;
/*     */   private XSFacet facet;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  34 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private RestrictionSimpleTypeImpl result; private Ref.SimpleType baseType; private Locator rloc;
/*     */   public SimpleType_Restriction(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  38 */     super(source, parent, cookie);
/*  39 */     this.$runtime = runtime;
/*  40 */     this.annotation = _annotation;
/*  41 */     this.locator = _locator;
/*  42 */     this.fa = _fa;
/*  43 */     this.name = _name;
/*  44 */     this.finalSet = _finalSet;
/*  45 */     this.$_ngcc_current_state = 13;
/*     */   }
/*     */   
/*     */   public SimpleType_Restriction(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, ForeignAttributesImpl _fa, String _name, Set _finalSet) {
/*  49 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _fa, _name, _finalSet);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  54 */     this.result.addFacet(this.facet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  60 */     this.result = new RestrictionSimpleTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, (this.name == null), this.finalSet, this.baseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  67 */     this.baseType = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.rloc, this.$runtime.currentSchema, this.baseTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  73 */     this.rloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  78 */     this.$uri = $__uri;
/*  79 */     this.$localName = $__local;
/*  80 */     this.$qname = $__qname;
/*  81 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  84 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  85 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 529);
/*  86 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  89 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/*  95 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  96 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  97 */           action3();
/*  98 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 101 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 107 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 108 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 540, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/* 109 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 112 */           this.$_ngcc_current_state = 5;
/* 113 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 119 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 120 */           this.$runtime.consumeAttribute($ai);
/* 121 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 124 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 125 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 534);
/* 126 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 129 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 12:
/* 136 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType"))) {
/* 137 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 542, this.fa);
/* 138 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 141 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 147 */         action1();
/* 148 */         this.$_ngcc_current_state = 2;
/* 149 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 154 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 155 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 530);
/* 156 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 159 */           this.$_ngcc_current_state = 1;
/* 160 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 166 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 171 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 179 */     this.$uri = $__uri;
/* 180 */     this.$localName = $__local;
/* 181 */     this.$qname = $__qname;
/* 182 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 185 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 186 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 187 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 190 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 196 */         this.$_ngcc_current_state = 5;
/* 197 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 202 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 203 */           this.$runtime.consumeAttribute($ai);
/* 204 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 207 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 213 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 214 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 542, this.fa);
/* 215 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 218 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 224 */         action1();
/* 225 */         this.$_ngcc_current_state = 2;
/* 226 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 231 */         this.$_ngcc_current_state = 1;
/* 232 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 237 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 242 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 250 */     this.$uri = $__uri;
/* 251 */     this.$localName = $__local;
/* 252 */     this.$qname = $__qname;
/* 253 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 256 */         this.$_ngcc_current_state = 5;
/* 257 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 262 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 263 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 266 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 272 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 273 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 542, this.fa);
/* 274 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 277 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 283 */         action1();
/* 284 */         this.$_ngcc_current_state = 2;
/* 285 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 290 */         this.$_ngcc_current_state = 1;
/* 291 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 296 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 301 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 309 */     this.$uri = $__uri;
/* 310 */     this.$localName = $__local;
/* 311 */     this.$qname = $__qname;
/* 312 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 315 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 316 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 319 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 325 */         this.$_ngcc_current_state = 5;
/* 326 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 331 */         action1();
/* 332 */         this.$_ngcc_current_state = 2;
/* 333 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 338 */         this.$_ngcc_current_state = 1;
/* 339 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 344 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 349 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 357 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 8:
/* 360 */         h = new qname(this, this._source, this.$runtime, 536);
/* 361 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 366 */         this.$_ngcc_current_state = 5;
/* 367 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 372 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 373 */           this.$runtime.consumeAttribute($ai);
/* 374 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 380 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 381 */           h = new foreignAttributes(this, this._source, this.$runtime, 542, this.fa);
/* 382 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 388 */         action1();
/* 389 */         this.$_ngcc_current_state = 2;
/* 390 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 395 */         this.$_ngcc_current_state = 1;
/* 396 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 401 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 408 */     switch ($__cookie__) {
/*     */       
/*     */       case 529:
/* 411 */         this.facet = (XSFacet)$__result__;
/* 412 */         action0();
/* 413 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 530:
/* 418 */         this.facet = (XSFacet)$__result__;
/* 419 */         action0();
/* 420 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 536:
/* 425 */         this.baseTypeName = (UName)$__result__;
/* 426 */         action2();
/* 427 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 540:
/* 432 */         this.annotation = (AnnotationImpl)$__result__;
/* 433 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 534:
/* 438 */         this.baseType = (Ref.SimpleType)$__result__;
/* 439 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 542:
/* 444 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 445 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 452 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\SimpleType_Restriction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
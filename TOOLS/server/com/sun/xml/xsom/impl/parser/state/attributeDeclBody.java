/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttributeDeclImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class attributeDeclBody extends NGCCHandler {
/*     */   private String name;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl annotation;
/*     */   private Locator locator;
/*     */   private boolean isLocal;
/*     */   private String defaultValue;
/*     */   private UName typeName;
/*     */   private String fixedValue;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private boolean form;
/*     */   private boolean formSpecified;
/*     */   private Ref.SimpleType type;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  35 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public attributeDeclBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, boolean _isLocal, String _defaultValue, String _fixedValue) {
/*  39 */     super(source, parent, cookie);
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
/* 491 */     this.formSpecified = false; this.$runtime = runtime; this.locator = _locator; this.isLocal = _isLocal;
/*     */     this.defaultValue = _defaultValue;
/*     */     this.fixedValue = _fixedValue;
/*     */     this.$_ngcc_current_state = 13; } private AttributeDeclImpl makeResult() { String tns;
/* 495 */     if (this.type == null)
/*     */     {
/* 497 */       this.type = (Ref.SimpleType)this.$runtime.parser.schemaSet.anySimpleType;
/*     */     }
/* 499 */     if (!this.formSpecified) this.form = this.$runtime.attributeFormDefault;
/*     */     
/* 501 */     if (!this.isLocal) this.form = true;
/*     */ 
/*     */     
/* 504 */     if (this.form == true) { tns = this.$runtime.currentSchema.getTargetNamespace(); }
/* 505 */     else { tns = ""; }
/*     */ 
/*     */     
/* 508 */     return new AttributeDeclImpl(this.$runtime.document, tns, this.name, this.annotation, this.locator, this.fa, this.isLocal, this.$runtime.createXmlString(this.defaultValue), this.$runtime.createXmlString(this.fixedValue), this.type); }
/*     */ 
/*     */   
/*     */   public attributeDeclBody(NGCCRuntimeEx runtime, Locator _locator, boolean _isLocal, String _defaultValue, String _fixedValue) {
/*     */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _locator, _isLocal, _defaultValue, _fixedValue);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     this.type = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.typeName);
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*     */     this.formSpecified = true;
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 12;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 9:
/*     */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType"))) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 7:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*     */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 338, null, AnnotationContext.ATTRIBUTE_DECL);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 1;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*     */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 329);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterElement($__qname);
/*     */   }
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 12;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 9:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 7:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         if ($__uri.equals("") && $__local.equals("form")) {
/*     */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 12;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 12:
/*     */         if ($__uri.equals("") && $__local.equals("name")) {
/*     */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 9:
/*     */         if ($__uri.equals("") && $__local.equals("type")) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 7:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri.equals("") && $__local.equals("type")) {
/*     */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     NGCCHandler h;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         this.$_ngcc_current_state = 12;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 10:
/*     */         if ($__uri.equals("") && $__local.equals("name")) {
/*     */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 9:
/*     */         h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */         spawnChildFromLeaveAttribute(h, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 7:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 4:
/*     */         if ($__uri.equals("") && $__local.equals("type")) {
/*     */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 14:
/*     */         if ($__uri.equals("") && $__local.equals("form")) {
/*     */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 11:
/*     */         this.name = $value;
/*     */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */       case 13:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 12;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */       case 15:
/*     */         if ($value.equals("unqualified")) {
/*     */           NGCCHandler nGCCHandler = new qualification(this, this._source, this.$runtime, 345);
/*     */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/*     */         if ($value.equals("qualified")) {
/*     */           NGCCHandler nGCCHandler = new qualification(this, this._source, this.$runtime, 345);
/*     */           spawnChildFromText(nGCCHandler, $value);
/*     */         } 
/*     */         break;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */       case 9:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*     */           NGCCHandler nGCCHandler = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/*     */         h = new foreignAttributes(this, this._source, this.$runtime, 340, this.fa);
/*     */         spawnChildFromText(h, $value);
/*     */         break;
/*     */       case 7:
/*     */         this.$_ngcc_current_state = 1;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 5:
/*     */         h = new qname(this, this._source, this.$runtime, 331);
/*     */         spawnChildFromText(h, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */     switch ($__cookie__) {
/*     */       case 345:
/*     */         this.form = ((Boolean)$__result__).booleanValue();
/*     */         action1();
/*     */         this.$_ngcc_current_state = 14;
/*     */         break;
/*     */       case 340:
/*     */         this.fa = (ForeignAttributesImpl)$__result__;
/*     */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */       case 338:
/*     */         this.annotation = (AnnotationImpl)$__result__;
/*     */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */       case 329:
/*     */         this.type = (Ref.SimpleType)$__result__;
/*     */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */       case 331:
/*     */         this.typeName = (UName)$__result__;
/*     */         action0();
/*     */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 7);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\attributeDeclBody.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
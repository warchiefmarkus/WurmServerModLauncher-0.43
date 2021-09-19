/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*     */ import com.sun.xml.xsom.impl.IdentityConstraintImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.XPathImpl;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ class identityConstraint
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private UName ref;
/*     */   private ForeignAttributesImpl fa;
/*     */   private AnnotationImpl ann;
/*     */   private XPathImpl field;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  32 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $uri; protected String $localName; protected String $qname; private short category; private List fields; private XPathImpl selector; private DelayedRef.IdentityConstraint refer;
/*     */   public identityConstraint(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  36 */     super(source, parent, cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 559 */     this.fields = new ArrayList();
/*     */     
/* 561 */     this.refer = null;
/*     */     this.$runtime = runtime;
/*     */     this.$_ngcc_current_state = 18; } private IdentityConstraintImpl makeResult() {
/* 564 */     return new IdentityConstraintImpl(this.$runtime.document, this.ann, this.$runtime.copyLocator(), this.fa, this.category, this.name, this.selector, this.fields, (Ref.IdentityConstraint)this.refer);
/*     */   }
/*     */   
/*     */   public identityConstraint(NGCCRuntimeEx runtime) {
/*     */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     this.fields.add(this.field);
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*     */     this.refer = new DelayedRef.IdentityConstraint((PatcherManager)this.$runtime, this.$runtime.copyLocator(), this.$runtime.currentSchema, this.ref);
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*     */     if (this.$localName.equals("key")) {
/*     */       this.category = 0;
/*     */     } else if (this.$localName.equals("keyref")) {
/*     */       this.category = 1;
/*     */     } else if (this.$localName.equals("unique")) {
/*     */       this.category = 2;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 10:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 653);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 16:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 17:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")))) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 666, null);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */       case 18:
/*     */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           action2();
/*     */           this.$_ngcc_current_state = 17;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 4:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*     */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 656, null, AnnotationContext.IDENTITY_CONSTRAINT);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 7;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 3:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 649);
/*     */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 7:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")) {
/*     */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*     */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
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
/*     */       case 2:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/*     */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*     */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 5:
/*     */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")) {
/*     */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*     */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 10:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 653);
/*     */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 16:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 17:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 666, null);
/*     */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/*     */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*     */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         this.$_ngcc_current_state = 7;
/*     */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 3:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 649);
/*     */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
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
/*     */       case 10:
/*     */         if ($__uri.equals("") && $__local.equals("refer")) {
/*     */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 6:
/*     */         if ($__uri.equals("") && $__local.equals("xpath")) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 653);
/*     */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 16:
/*     */         if ($__uri.equals("") && $__local.equals("name")) {
/*     */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 17:
/*     */         if ($__uri.equals("") && $__local.equals("name")) {
/*     */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 666, null);
/*     */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 8:
/*     */         this.$_ngcc_current_state = 7;
/*     */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 3:
/*     */         if ($__uri.equals("") && $__local.equals("xpath")) {
/*     */           NGCCHandler h = new xpath(this, this._source, this.$runtime, 649);
/*     */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 10:
/*     */         this.$_ngcc_current_state = 8;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 14:
/*     */         if ($__uri.equals("") && $__local.equals("name")) {
/*     */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 11:
/*     */         if ($__uri.equals("") && $__local.equals("refer")) {
/*     */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         this.$_ngcc_current_state = 7;
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
/*     */       case 12:
/*     */         h = new qname(this, this._source, this.$runtime, 659);
/*     */         spawnChildFromText(h, $value);
/*     */         break;
/*     */       case 10:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "refer")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 8;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 15:
/*     */         this.name = $value;
/*     */         this.$_ngcc_current_state = 14;
/*     */         break;
/*     */       case 6:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/*     */           h = new xpath(this, this._source, this.$runtime, 653);
/*     */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */       case 16:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */       case 17:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           h = new foreignAttributes(this, this._source, this.$runtime, 666, null);
/*     */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */       case 8:
/*     */         this.$_ngcc_current_state = 7;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 3:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "xpath")) >= 0) {
/*     */           h = new xpath(this, this._source, this.$runtime, 649);
/*     */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*     */     switch ($__cookie__) {
/*     */       case 666:
/*     */         this.fa = (ForeignAttributesImpl)$__result__;
/*     */         this.$_ngcc_current_state = 16;
/*     */         break;
/*     */       case 656:
/*     */         this.ann = (AnnotationImpl)$__result__;
/*     */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */       case 659:
/*     */         this.ref = (UName)$__result__;
/*     */         action1();
/*     */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */       case 653:
/*     */         this.selector = (XPathImpl)$__result__;
/*     */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */       case 649:
/*     */         this.field = (XPathImpl)$__result__;
/*     */         action0();
/*     */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\identityConstraint.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
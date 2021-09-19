/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ElementDecl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.xsom.impl.ParticleImpl;
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
/*     */ class particle
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private ElementDecl anonymousElementDecl;
/*     */   private WildcardImpl wcBody;
/*     */   private ModelGroupImpl term;
/*     */   private UName elementTypeName;
/*     */   private occurs occurs;
/*     */   private UName groupName;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  34 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private Locator wloc; private Locator loc; private ParticleImpl result; private String compositorName;
/*     */   public particle(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  38 */     super(source, parent, cookie);
/*  39 */     this.$runtime = runtime;
/*  40 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public particle(NGCCRuntimeEx runtime) {
/*  44 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  49 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.wcBody, this.wloc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  54 */     this.wloc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  59 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.anonymousElementDecl, this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  67 */     this.result = new ParticleImpl(this.$runtime.document, this.annotation, (Ref.Term)new DelayedRef.Element((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.elementTypeName), this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/*  74 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action5() throws SAXException {
/*  79 */     this.result = new ParticleImpl(this.$runtime.document, this.annotation, (Ref.Term)new DelayedRef.ModelGroup((PatcherManager)this.$runtime, this.loc, this.$runtime.currentSchema, this.groupName), this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action6() throws SAXException {
/*  86 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   private void action7() throws SAXException {
/*  91 */     this.result = new ParticleImpl(this.$runtime.document, null, (Ref.Term)this.term, this.loc, this.occurs.max, this.occurs.min);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action8() throws SAXException {
/*  97 */     this.compositorName = this.$localName;
/*  98 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/* 104 */     this.$uri = $__uri;
/* 105 */     this.$localName = $__local;
/* 106 */     this.$qname = $__qname;
/* 107 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 26:
/* 110 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 111 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 599);
/* 112 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 115 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 121 */         if ((($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 122 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 574);
/* 123 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 126 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 132 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 133 */           this.$runtime.consumeAttribute($ai);
/* 134 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 137 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 143 */         action5();
/* 144 */         this.$_ngcc_current_state = 19;
/* 145 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 150 */         if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))))) {
/* 151 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 588);
/* 152 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 155 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 161 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 162 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 163 */           action8();
/* 164 */           this.$_ngcc_current_state = 30;
/*     */         
/*     */         }
/* 167 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 168 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 169 */           action6();
/* 170 */           this.$_ngcc_current_state = 26;
/*     */         
/*     */         }
/* 173 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/* 174 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 175 */           action4();
/* 176 */           this.$_ngcc_current_state = 16;
/*     */         
/*     */         }
/* 179 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/* 180 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 181 */           action1();
/* 182 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 185 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 21:
/* 194 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 195 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 594, null, AnnotationContext.PARTICLE);
/* 196 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 199 */           this.$_ngcc_current_state = 20;
/* 200 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 206 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/* 207 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 603, this.loc, this.compositorName);
/* 208 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 211 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 217 */         action3();
/* 218 */         this.$_ngcc_current_state = 7;
/* 219 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 224 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")))) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/* 225 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 604);
/* 226 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 229 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 235 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 236 */           this.$runtime.consumeAttribute($ai);
/* 237 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 240 */         else if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 241 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 242 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 245 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 252 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 257 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 258 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 573, this.wloc);
/* 259 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 262 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 268 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 269 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 582, null, AnnotationContext.PARTICLE);
/* 270 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 273 */           this.$_ngcc_current_state = 10;
/* 274 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 280 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 288 */     this.$uri = $__uri;
/* 289 */     this.$localName = $__local;
/* 290 */     this.$qname = $__qname;
/* 291 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 26:
/* 294 */         if ((($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/* 295 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 599);
/* 296 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 299 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 28:
/* 305 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 306 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 307 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 310 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 316 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/* 317 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 574);
/* 318 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 321 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 327 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) {
/* 328 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 329 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 332 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 338 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 339 */           this.$runtime.consumeAttribute($ai);
/* 340 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 343 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 349 */         action5();
/* 350 */         this.$_ngcc_current_state = 19;
/* 351 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 356 */         if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/* 357 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 588);
/* 358 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 361 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 367 */         this.$_ngcc_current_state = 20;
/* 368 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 373 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 374 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 375 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 378 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 384 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 385 */           NGCCHandler h = new modelGroupBody(this, this._source, this.$runtime, 603, this.loc, this.compositorName);
/* 386 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 389 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 395 */         action3();
/* 396 */         this.$_ngcc_current_state = 7;
/* 397 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 402 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")))) || (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0 && (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))))) {
/* 403 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 604);
/* 404 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 407 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 413 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) {
/* 414 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 415 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 418 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 424 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 425 */           this.$runtime.consumeAttribute($ai);
/* 426 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         
/*     */         }
/* 429 */         else if ((($ai = this.$runtime.getAttributeIndex("", "default")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element"))) {
/* 430 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 431 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 434 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 441 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 446 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0 && $__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any"))) {
/* 447 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 573, this.wloc);
/* 448 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 451 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 457 */         this.$_ngcc_current_state = 10;
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
/*     */       case 4:
/* 477 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 478 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 574);
/* 479 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 482 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 488 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("ref"))) {
/* 489 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 599);
/* 490 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 493 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 499 */         action3();
/* 500 */         this.$_ngcc_current_state = 7;
/* 501 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 506 */         if (($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs"))) {
/* 507 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 604);
/* 508 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 511 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 517 */         action5();
/* 518 */         this.$_ngcc_current_state = 19;
/* 519 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 524 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 525 */           this.$_ngcc_current_state = 24;
/*     */         } else {
/*     */           
/* 528 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 534 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 535 */           this.$_ngcc_current_state = 14;
/*     */         
/*     */         }
/* 538 */         else if (($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("name"))) {
/* 539 */           NGCCHandler h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 540 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 543 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 550 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 555 */         if (($__uri.equals("") && $__local.equals("default")) || ($__uri.equals("") && $__local.equals("block")) || ($__uri.equals("") && $__local.equals("fixed")) || ($__uri.equals("") && $__local.equals("maxOccurs")) || ($__uri.equals("") && $__local.equals("minOccurs")) || ($__uri.equals("") && $__local.equals("form")) || ($__uri.equals("") && $__local.equals("final")) || ($__uri.equals("") && $__local.equals("abstract")) || ($__uri.equals("") && $__local.equals("ref")) || ($__uri.equals("") && $__local.equals("name"))) {
/* 556 */           NGCCHandler h = new occurs(this, this._source, this.$runtime, 588);
/* 557 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 560 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 566 */         if (($__uri.equals("") && $__local.equals("processContents")) || ($__uri.equals("") && $__local.equals("namespace"))) {
/* 567 */           NGCCHandler h = new wildcardBody(this, this._source, this.$runtime, 573, this.wloc);
/* 568 */           spawnChildFromEnterAttribute(h, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 571 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 577 */         this.$_ngcc_current_state = 20;
/* 578 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 583 */         this.$_ngcc_current_state = 10;
/* 584 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 589 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 597 */     this.$uri = $__uri;
/* 598 */     this.$localName = $__local;
/* 599 */     this.$qname = $__qname;
/* 600 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 23:
/* 603 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 604 */           this.$_ngcc_current_state = 21;
/*     */         } else {
/*     */           
/* 607 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 613 */         action3();
/* 614 */         this.$_ngcc_current_state = 7;
/* 615 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 20:
/* 620 */         action5();
/* 621 */         this.$_ngcc_current_state = 19;
/* 622 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 627 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 632 */         this.$_ngcc_current_state = 20;
/* 633 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 638 */         this.$_ngcc_current_state = 10;
/* 639 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 644 */         if ($__uri.equals("") && $__local.equals("ref")) {
/* 645 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 648 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 654 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     NGCCHandler h;
/* 662 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 26:
/* 665 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 666 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 599);
/* 667 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 670 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 671 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 599);
/* 672 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 675 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 676 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 599);
/* 677 */           spawnChildFromText(nGCCHandler, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 685 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 686 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 574);
/* 687 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 690 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 691 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 574);
/* 692 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 695 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 696 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 574);
/* 697 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 700 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 701 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 574);
/* 702 */           spawnChildFromText(nGCCHandler, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 25:
/* 711 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 712 */           this.$runtime.consumeAttribute($ai);
/* 713 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 20:
/* 719 */         action5();
/* 720 */         this.$_ngcc_current_state = 19;
/* 721 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 16:
/* 726 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 727 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 728 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 731 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 732 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 733 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 736 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 737 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 738 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 741 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 742 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 743 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 746 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 747 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 748 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 751 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 752 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 753 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 756 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 757 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 758 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 761 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 762 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 763 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 766 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 767 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 768 */           spawnChildFromText(nGCCHandler, $value);
/*     */           break;
/*     */         } 
/* 771 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 772 */           NGCCHandler nGCCHandler = new occurs(this, this._source, this.$runtime, 588);
/* 773 */           spawnChildFromText(nGCCHandler, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 21:
/* 788 */         this.$_ngcc_current_state = 20;
/* 789 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 794 */         action3();
/* 795 */         this.$_ngcc_current_state = 7;
/* 796 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 801 */         h = new qname(this, this._source, this.$runtime, 585);
/* 802 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 30:
/* 807 */         if (($ai = this.$runtime.getAttributeIndex("", "minOccurs")) >= 0) {
/* 808 */           h = new occurs(this, this._source, this.$runtime, 604);
/* 809 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 812 */         if (($ai = this.$runtime.getAttributeIndex("", "maxOccurs")) >= 0) {
/* 813 */           h = new occurs(this, this._source, this.$runtime, 604);
/* 814 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 8:
/* 821 */         if (($ai = this.$runtime.getAttributeIndex("", "ref")) >= 0) {
/* 822 */           this.$runtime.consumeAttribute($ai);
/* 823 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 826 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 827 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 828 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 831 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 832 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 833 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 836 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 837 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 838 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 841 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/* 842 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 843 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 846 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 847 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 848 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 851 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 852 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 853 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 856 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/* 857 */           h = new elementDeclBody(this, this._source, this.$runtime, 579, this.loc, false);
/* 858 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 871 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 876 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 877 */           h = new wildcardBody(this, this._source, this.$runtime, 573, this.wloc);
/* 878 */           spawnChildFromText(h, $value);
/*     */           break;
/*     */         } 
/* 881 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 882 */           h = new wildcardBody(this, this._source, this.$runtime, 573, this.wloc);
/* 883 */           spawnChildFromText(h, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 24:
/* 890 */         h = new qname(this, this._source, this.$runtime, 597);
/* 891 */         spawnChildFromText(h, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 896 */         this.$_ngcc_current_state = 10;
/* 897 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 904 */     switch ($__cookie__) {
/*     */       
/*     */       case 599:
/* 907 */         this.occurs = (occurs)$__result__;
/* 908 */         this.$_ngcc_current_state = 25;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 579:
/* 913 */         this.anonymousElementDecl = (ElementDecl)$__result__;
/* 914 */         action2();
/* 915 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 573:
/* 920 */         this.wcBody = (WildcardImpl)$__result__;
/* 921 */         action0();
/* 922 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 597:
/* 927 */         this.groupName = (UName)$__result__;
/* 928 */         this.$_ngcc_current_state = 23;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 582:
/* 933 */         this.annotation = (AnnotationImpl)$__result__;
/* 934 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 574:
/* 939 */         this.occurs = (occurs)$__result__;
/* 940 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 588:
/* 945 */         this.occurs = (occurs)$__result__;
/* 946 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 594:
/* 951 */         this.annotation = (AnnotationImpl)$__result__;
/* 952 */         this.$_ngcc_current_state = 20;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 603:
/* 957 */         this.term = (ModelGroupImpl)$__result__;
/* 958 */         action7();
/* 959 */         this.$_ngcc_current_state = 28;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 585:
/* 964 */         this.elementTypeName = (UName)$__result__;
/* 965 */         this.$_ngcc_current_state = 13;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 604:
/* 970 */         this.occurs = (occurs)$__result__;
/* 971 */         this.$_ngcc_current_state = 29;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 978 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\particle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
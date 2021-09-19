/*      */ package com.sun.xml.xsom.impl.parser.state;
/*      */ import com.sun.xml.xsom.XSComplexType;
/*      */ import com.sun.xml.xsom.XSContentType;
/*      */ import com.sun.xml.xsom.XSFacet;
/*      */ import com.sun.xml.xsom.XSSimpleType;
/*      */ import com.sun.xml.xsom.XSType;
/*      */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.xsom.impl.AttributesHolder;
/*      */ import com.sun.xml.xsom.impl.ComplexTypeImpl;
/*      */ import com.sun.xml.xsom.impl.ContentTypeImpl;
/*      */ import com.sun.xml.xsom.impl.ForeignAttributesImpl;
/*      */ import com.sun.xml.xsom.impl.ParticleImpl;
/*      */ import com.sun.xml.xsom.impl.Ref;
/*      */ import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;
/*      */ import com.sun.xml.xsom.impl.UName;
/*      */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*      */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*      */ import com.sun.xml.xsom.parser.AnnotationContext;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ class complexType extends NGCCHandler {
/*      */   private Integer finalValue;
/*      */   private String name;
/*      */   private String abstractValue;
/*      */   private Integer blockValue;
/*      */   private XSFacet facet;
/*      */   private ForeignAttributesImpl fa;
/*      */   private AnnotationImpl annotation;
/*      */   private ContentTypeImpl explicitContent;
/*      */   private UName baseTypeName;
/*      */   private String mixedValue;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   37 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private ComplexTypeImpl result; private Ref.Type baseType; private Ref.ContentType contentType; private Ref.SimpleType baseContentType; private RestrictionSimpleTypeImpl contentSimpleType; private Locator locator; private Locator locator2;
/*      */   public complexType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*   41 */     super(source, parent, cookie);
/*   42 */     this.$runtime = runtime;
/*   43 */     this.$_ngcc_current_state = 88;
/*      */   }
/*      */   
/*      */   public complexType(NGCCRuntimeEx runtime) {
/*   47 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action0() throws SAXException {
/*   52 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action1() throws SAXException {
/*   58 */     this.baseType = (Ref.Type)this.$runtime.parser.schemaSet.anyType;
/*   59 */     makeResult(2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action2() throws SAXException {
/*   65 */     this.result.setExplicitContent((XSContentType)this.explicitContent);
/*   66 */     this.result.setContentType(buildComplexExtensionContentModel((XSContentType)this.explicitContent));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action3() throws SAXException {
/*   73 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*   75 */     makeResult(1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action4() throws SAXException {
/*   80 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action5() throws SAXException {
/*   85 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action6() throws SAXException {
/*   91 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*   93 */     makeResult(2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action7() throws SAXException {
/*   98 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action8() throws SAXException {
/*  103 */     this.contentType = (Ref.ContentType)new BaseContentRef(this.$runtime, this.baseType);
/*  104 */     makeResult(1);
/*  105 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action9() throws SAXException {
/*  111 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action10() throws SAXException {
/*  117 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action11() throws SAXException {
/*  122 */     makeResult(2);
/*  123 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action12() throws SAXException {
/*  128 */     this.contentSimpleType.addFacet(this.facet);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action13() throws SAXException {
/*  133 */     if (this.baseContentType == null)
/*      */     {
/*  135 */       this.baseContentType = new BaseContentSimpleTypeRef(this.baseType);
/*      */     }
/*      */     
/*  138 */     this.contentSimpleType = new RestrictionSimpleTypeImpl(this.$runtime.document, null, this.locator2, null, null, true, Collections.EMPTY_SET, this.baseContentType);
/*      */ 
/*      */     
/*  141 */     this.contentType = (Ref.ContentType)this.contentSimpleType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action14() throws SAXException {
/*  147 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action15() throws SAXException {
/*  153 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   private void action16() throws SAXException {
/*  157 */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*  162 */     this.$uri = $__uri;
/*  163 */     this.$localName = $__local;
/*  164 */     this.$qname = $__qname;
/*  165 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 7:
/*  168 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  169 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  170 */           action7();
/*  171 */           this.$_ngcc_current_state = 24;
/*      */         
/*      */         }
/*  174 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  175 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  176 */           action4();
/*  177 */           this.$_ngcc_current_state = 15;
/*      */         } else {
/*      */           
/*  180 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 38:
/*  187 */         action8();
/*  188 */         this.$_ngcc_current_state = 37;
/*  189 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  194 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  195 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 257);
/*  196 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  199 */           action11();
/*  200 */           this.$_ngcc_current_state = 47;
/*  201 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/*  207 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  208 */           this.$runtime.consumeAttribute($ai);
/*  209 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  212 */           this.$_ngcc_current_state = 68;
/*  213 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  219 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  220 */           this.$runtime.consumeAttribute($ai);
/*  221 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  224 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/*  230 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  231 */           NGCCHandler h = new facet(this, this._source, this.$runtime, 258);
/*  232 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  235 */           this.$_ngcc_current_state = 48;
/*  236 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  242 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  243 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 219, (AttributesHolder)this.result);
/*  244 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  247 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 67:
/*  253 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent"))) {
/*  254 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 284, this.fa);
/*  255 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  258 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 88:
/*  264 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  265 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  266 */           action16();
/*  267 */           this.$_ngcc_current_state = 84;
/*      */         } else {
/*      */           
/*  270 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/*  276 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  277 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 274, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  278 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  281 */           this.$_ngcc_current_state = 35;
/*  282 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  288 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension"))) {
/*  289 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 232, this.fa);
/*  290 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  293 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/*  299 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  300 */           this.$runtime.consumeAttribute($ai);
/*  301 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  304 */           this.$_ngcc_current_state = 76;
/*  305 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 12:
/*  311 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  312 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 212, this.fa);
/*  313 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  316 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/*  322 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  323 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  324 */           action15();
/*  325 */           this.$_ngcc_current_state = 59;
/*      */         
/*      */         }
/*  328 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  329 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  330 */           action10();
/*  331 */           this.$_ngcc_current_state = 44;
/*      */         } else {
/*      */           
/*  334 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 76:
/*  341 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  342 */           this.$runtime.consumeAttribute($ai);
/*  343 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  346 */           this.$_ngcc_current_state = 72;
/*  347 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/*  353 */         action13();
/*  354 */         this.$_ngcc_current_state = 49;
/*  355 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  360 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  361 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 254, (AttributesHolder)this.result);
/*  362 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  365 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/*  371 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  372 */           this.$runtime.consumeAttribute($ai);
/*  373 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  376 */           this.$_ngcc_current_state = 67;
/*  377 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/*  383 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  384 */           this.$runtime.consumeAttribute($ai);
/*  385 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  388 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 37:
/*  394 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  395 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 242, (AttributesHolder)this.result);
/*  396 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  399 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  405 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  406 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 230, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  407 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  410 */           this.$_ngcc_current_state = 7;
/*  411 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  417 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  418 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 267, this.fa);
/*  419 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  422 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  428 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  429 */           this.$runtime.consumeAttribute($ai);
/*  430 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  433 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  439 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  440 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 210, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  441 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  444 */           this.$_ngcc_current_state = 9;
/*  445 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 41:
/*  451 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute"))) {
/*  452 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 247, this.fa);
/*  453 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  456 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/*  462 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  463 */           this.$runtime.consumeAttribute($ai);
/*  464 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  467 */           this.$_ngcc_current_state = 80;
/*  468 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  474 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  475 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  476 */           this.$_ngcc_current_state = 63;
/*      */         
/*      */         }
/*  479 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  480 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  481 */           this.$_ngcc_current_state = 29;
/*      */         
/*      */         }
/*  484 */         else if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  485 */           action1();
/*  486 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 205, (AttributesHolder)this.result);
/*  487 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  490 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 65:
/*  498 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  499 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 282, null, AnnotationContext.COMPLEXTYPE_DECL);
/*  500 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  503 */           this.$_ngcc_current_state = 2;
/*  504 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  510 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  511 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 223, this.fa);
/*  512 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  515 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/*  521 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  522 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 265, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  523 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  526 */           this.$_ngcc_current_state = 52;
/*  527 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/*  533 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  534 */           NGCCHandler h = new simpleType(this, this._source, this.$runtime, 262);
/*  535 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  538 */           this.$_ngcc_current_state = 51;
/*  539 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/*  545 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  546 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 245, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  547 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  550 */           this.$_ngcc_current_state = 38;
/*  551 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  557 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/*  562 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  563 */           this.$runtime.consumeAttribute($ai);
/*  564 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  567 */           this.$_ngcc_current_state = 28;
/*  568 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  574 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group"))) {
/*  575 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 208, (AttributesHolder)this.result);
/*  576 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  579 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/*  585 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  586 */           NGCCHandler h = new annotation(this, this._source, this.$runtime, 221, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  587 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  590 */           this.$_ngcc_current_state = 18;
/*  591 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  597 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/*  598 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 276, this.fa);
/*  599 */           spawnChildFromEnterElement(h, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  602 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  608 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  609 */           this.$runtime.consumeAttribute($ai);
/*  610 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  613 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  619 */     unexpectedEnterElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*  627 */     this.$uri = $__uri;
/*  628 */     this.$localName = $__local;
/*  629 */     this.$qname = $__qname;
/*  630 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 38:
/*  633 */         action8();
/*  634 */         this.$_ngcc_current_state = 37;
/*  635 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  640 */         action11();
/*  641 */         this.$_ngcc_current_state = 47;
/*  642 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 6:
/*  647 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  648 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  649 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  652 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  658 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  659 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  660 */           this.$_ngcc_current_state = 34;
/*      */         } else {
/*      */           
/*  663 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  669 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  670 */           this.$runtime.consumeAttribute($ai);
/*  671 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  674 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/*  680 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  681 */           this.$runtime.consumeAttribute($ai);
/*  682 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  685 */           this.$_ngcc_current_state = 68;
/*  686 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/*  692 */         this.$_ngcc_current_state = 48;
/*  693 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  698 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  699 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 219, (AttributesHolder)this.result);
/*  700 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  703 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 67:
/*  709 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  710 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 284, this.fa);
/*  711 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  714 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/*  720 */         this.$_ngcc_current_state = 35;
/*  721 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 34:
/*  726 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  727 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  728 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  731 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/*  737 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  738 */           this.$runtime.consumeAttribute($ai);
/*  739 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  742 */           this.$_ngcc_current_state = 76;
/*  743 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 12:
/*  749 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  750 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 212, this.fa);
/*  751 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  754 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/*  760 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  761 */           this.$runtime.consumeAttribute($ai);
/*  762 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  765 */           this.$_ngcc_current_state = 72;
/*  766 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/*  772 */         action13();
/*  773 */         this.$_ngcc_current_state = 49;
/*  774 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  779 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  780 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 254, (AttributesHolder)this.result);
/*  781 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  784 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/*  790 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  791 */           this.$runtime.consumeAttribute($ai);
/*  792 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  795 */           this.$_ngcc_current_state = 67;
/*  796 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/*  802 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  803 */           this.$runtime.consumeAttribute($ai);
/*  804 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  807 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 37:
/*  813 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  814 */           NGCCHandler h = new attributeUses(this, this._source, this.$runtime, 242, (AttributesHolder)this.result);
/*  815 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  818 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 46:
/*  824 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  825 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  826 */           this.$_ngcc_current_state = 34;
/*      */         } else {
/*      */           
/*  829 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  835 */         this.$_ngcc_current_state = 7;
/*  836 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  841 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  842 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 267, this.fa);
/*  843 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  846 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  852 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  853 */           this.$runtime.consumeAttribute($ai);
/*  854 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  857 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  863 */         this.$_ngcc_current_state = 9;
/*  864 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 41:
/*  869 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  870 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 247, this.fa);
/*  871 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  874 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/*  880 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  881 */           this.$runtime.consumeAttribute($ai);
/*  882 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  885 */           this.$_ngcc_current_state = 80;
/*  886 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  892 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  893 */           action1();
/*  894 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 205, (AttributesHolder)this.result);
/*  895 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  898 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/*  904 */         this.$_ngcc_current_state = 2;
/*  905 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  910 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  911 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  912 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  915 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  921 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  922 */           NGCCHandler h = new foreignAttributes(this, this._source, this.$runtime, 223, this.fa);
/*  923 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  926 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/*  932 */         this.$_ngcc_current_state = 52;
/*  933 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/*  938 */         this.$_ngcc_current_state = 51;
/*  939 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/*  944 */         this.$_ngcc_current_state = 38;
/*  945 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  950 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  951 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  952 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  955 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  961 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  962 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  963 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  966 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  972 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/*  977 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  978 */           this.$runtime.consumeAttribute($ai);
/*  979 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  982 */           this.$_ngcc_current_state = 28;
/*  983 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  989 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  990 */           NGCCHandler h = new complexType_complexContent_body(this, this._source, this.$runtime, 208, (AttributesHolder)this.result);
/*  991 */           spawnChildFromLeaveElement(h, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  994 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/* 1000 */         this.$_ngcc_current_state = 18;
/* 1001 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/* 1006 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1007 */           this.$runtime.consumeAttribute($ai);
/* 1008 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/* 1011 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1017 */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 1025 */     this.$uri = $__uri;
/* 1026 */     this.$localName = $__local;
/* 1027 */     this.$qname = $__qname;
/* 1028 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 68:
/* 1031 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1032 */           this.$_ngcc_current_state = 70;
/*      */         } else {
/*      */           
/* 1035 */           this.$_ngcc_current_state = 67;
/* 1036 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/* 1042 */         action8();
/* 1043 */         this.$_ngcc_current_state = 37;
/* 1044 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1049 */         action11();
/* 1050 */         this.$_ngcc_current_state = 47;
/* 1051 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 15:
/* 1056 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1057 */           this.$_ngcc_current_state = 14;
/*      */         } else {
/*      */           
/* 1060 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/* 1066 */         this.$_ngcc_current_state = 7;
/* 1067 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/* 1072 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1073 */           this.$_ngcc_current_state = 23;
/*      */         } else {
/*      */           
/* 1076 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1082 */         this.$_ngcc_current_state = 9;
/* 1083 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/* 1088 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/* 1089 */           this.$_ngcc_current_state = 86;
/*      */         } else {
/*      */           
/* 1092 */           this.$_ngcc_current_state = 80;
/* 1093 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/* 1099 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1100 */           this.$_ngcc_current_state = 43;
/*      */         } else {
/*      */           
/* 1103 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/* 1109 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1110 */           this.$_ngcc_current_state = 74;
/*      */         } else {
/*      */           
/* 1113 */           this.$_ngcc_current_state = 68;
/* 1114 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/* 1120 */         this.$_ngcc_current_state = 48;
/* 1121 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1126 */         this.$_ngcc_current_state = 2;
/* 1127 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/* 1132 */         this.$_ngcc_current_state = 52;
/* 1133 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1138 */         this.$_ngcc_current_state = 51;
/* 1139 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/* 1144 */         this.$_ngcc_current_state = 38;
/* 1145 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1150 */         this.$_ngcc_current_state = 35;
/* 1151 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1156 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/* 1161 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1162 */           this.$_ngcc_current_state = 82;
/*      */         } else {
/*      */           
/* 1165 */           this.$_ngcc_current_state = 76;
/* 1166 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/* 1172 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1173 */           this.$_ngcc_current_state = 31;
/*      */         } else {
/*      */           
/* 1176 */           this.$_ngcc_current_state = 28;
/* 1177 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/* 1183 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 1184 */           this.$_ngcc_current_state = 78;
/*      */         } else {
/*      */           
/* 1187 */           this.$_ngcc_current_state = 72;
/* 1188 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/* 1194 */         action13();
/* 1195 */         this.$_ngcc_current_state = 49;
/* 1196 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/* 1201 */         this.$_ngcc_current_state = 18;
/* 1202 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/* 1207 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1208 */           this.$_ngcc_current_state = 58;
/*      */         } else {
/*      */           
/* 1211 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1217 */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 1225 */     this.$uri = $__uri;
/* 1226 */     this.$localName = $__local;
/* 1227 */     this.$qname = $__qname;
/* 1228 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 81:
/* 1231 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1232 */           this.$_ngcc_current_state = 76;
/*      */         } else {
/*      */           
/* 1235 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 22:
/* 1241 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1242 */           this.$_ngcc_current_state = 21;
/*      */         } else {
/*      */           
/* 1245 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/* 1251 */         action8();
/* 1252 */         this.$_ngcc_current_state = 37;
/* 1253 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1258 */         action11();
/* 1259 */         this.$_ngcc_current_state = 47;
/* 1260 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/* 1265 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1266 */           this.$_ngcc_current_state = 68;
/*      */         } else {
/*      */           
/* 1269 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 30:
/* 1275 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1276 */           this.$_ngcc_current_state = 28;
/*      */         } else {
/*      */           
/* 1279 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 72:
/* 1285 */         this.$_ngcc_current_state = 68;
/* 1286 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 49:
/* 1291 */         this.$_ngcc_current_state = 48;
/* 1292 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1297 */         this.$_ngcc_current_state = 35;
/* 1298 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 80:
/* 1303 */         this.$_ngcc_current_state = 76;
/* 1304 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/* 1309 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1310 */           this.$_ngcc_current_state = 67;
/*      */         } else {
/*      */           
/* 1313 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 76:
/* 1319 */         this.$_ngcc_current_state = 72;
/* 1320 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 51:
/* 1325 */         action13();
/* 1326 */         this.$_ngcc_current_state = 49;
/* 1327 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/* 1332 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 1333 */           this.$_ngcc_current_state = 72;
/*      */         } else {
/*      */           
/* 1336 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 68:
/* 1342 */         this.$_ngcc_current_state = 67;
/* 1343 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 85:
/* 1348 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/* 1349 */           this.$_ngcc_current_state = 80;
/*      */         } else {
/*      */           
/* 1352 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/* 1358 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1359 */           this.$_ngcc_current_state = 12;
/*      */         } else {
/*      */           
/* 1362 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 42:
/* 1368 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1369 */           this.$_ngcc_current_state = 41;
/*      */         } else {
/*      */           
/* 1372 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1378 */         this.$_ngcc_current_state = 9;
/* 1379 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/* 1384 */         this.$_ngcc_current_state = 7;
/* 1385 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 84:
/* 1390 */         this.$_ngcc_current_state = 80;
/* 1391 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1396 */         this.$_ngcc_current_state = 2;
/* 1397 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/* 1402 */         this.$_ngcc_current_state = 52;
/* 1403 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1408 */         this.$_ngcc_current_state = 51;
/* 1409 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 39:
/* 1414 */         this.$_ngcc_current_state = 38;
/* 1415 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 57:
/* 1420 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1421 */           this.$_ngcc_current_state = 56;
/*      */         } else {
/*      */           
/* 1424 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1430 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/* 1435 */         this.$_ngcc_current_state = 28;
/* 1436 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 19:
/* 1441 */         this.$_ngcc_current_state = 18;
/* 1442 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1447 */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     NGCCHandler h;
/* 1455 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 38:
/* 1458 */         action8();
/* 1459 */         this.$_ngcc_current_state = 37;
/* 1460 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 48:
/* 1465 */         action11();
/* 1466 */         this.$_ngcc_current_state = 47;
/* 1467 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 70:
/* 1472 */         this.name = $value;
/* 1473 */         this.$_ngcc_current_state = 69;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 82:
/* 1478 */         h = new erSet(this, this._source, this.$runtime, 301);
/* 1479 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 78:
/* 1484 */         h = new erSet(this, this._source, this.$runtime, 296);
/* 1485 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 72:
/* 1490 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1491 */           this.$runtime.consumeAttribute($ai);
/* 1492 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1495 */         this.$_ngcc_current_state = 68;
/* 1496 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 44:
/* 1502 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1503 */           this.$runtime.consumeAttribute($ai);
/* 1504 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 49:
/* 1510 */         this.$_ngcc_current_state = 48;
/* 1511 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 31:
/* 1516 */         this.mixedValue = $value;
/* 1517 */         this.$_ngcc_current_state = 30;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 61:
/* 1522 */         this.$_ngcc_current_state = 35;
/* 1523 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 80:
/* 1528 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 1529 */           this.$runtime.consumeAttribute($ai);
/* 1530 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1533 */         this.$_ngcc_current_state = 76;
/* 1534 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 14:
/* 1540 */         h = new qname(this, this._source, this.$runtime, 214);
/* 1541 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 74:
/* 1546 */         this.mixedValue = $value;
/* 1547 */         this.$_ngcc_current_state = 73;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 76:
/* 1552 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1553 */           this.$runtime.consumeAttribute($ai);
/* 1554 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1557 */         this.$_ngcc_current_state = 72;
/* 1558 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 51:
/* 1564 */         action13();
/* 1565 */         this.$_ngcc_current_state = 49;
/* 1566 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 68:
/* 1571 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1572 */           this.$runtime.consumeAttribute($ai);
/* 1573 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1576 */         this.$_ngcc_current_state = 67;
/* 1577 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 15:
/* 1583 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1584 */           this.$runtime.consumeAttribute($ai);
/* 1585 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 58:
/* 1591 */         h = new qname(this, this._source, this.$runtime, 269);
/* 1592 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 26:
/* 1597 */         this.$_ngcc_current_state = 7;
/* 1598 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 10:
/* 1603 */         this.$_ngcc_current_state = 9;
/* 1604 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 24:
/* 1609 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1610 */           this.$runtime.consumeAttribute($ai);
/* 1611 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 84:
/* 1617 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 1618 */           this.$runtime.consumeAttribute($ai);
/* 1619 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1622 */         this.$_ngcc_current_state = 80;
/* 1623 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 65:
/* 1629 */         this.$_ngcc_current_state = 2;
/* 1630 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 54:
/* 1635 */         this.$_ngcc_current_state = 52;
/* 1636 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 52:
/* 1641 */         this.$_ngcc_current_state = 51;
/* 1642 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 39:
/* 1647 */         this.$_ngcc_current_state = 38;
/* 1648 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 0:
/* 1653 */         revertToParentFromText(this.result, this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 86:
/* 1658 */         this.abstractValue = $value;
/* 1659 */         this.$_ngcc_current_state = 85;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 23:
/* 1664 */         h = new qname(this, this._source, this.$runtime, 225);
/* 1665 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 29:
/* 1670 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1671 */           this.$runtime.consumeAttribute($ai);
/* 1672 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1675 */         this.$_ngcc_current_state = 28;
/* 1676 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 43:
/* 1682 */         h = new qname(this, this._source, this.$runtime, 249);
/* 1683 */         spawnChildFromText(h, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 19:
/* 1688 */         this.$_ngcc_current_state = 18;
/* 1689 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 59:
/* 1694 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1695 */           this.$runtime.consumeAttribute($ai);
/* 1696 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 1704 */     switch ($__cookie__) {
/*      */       
/*      */       case 257:
/* 1707 */         this.facet = (XSFacet)$__result__;
/* 1708 */         action12();
/* 1709 */         this.$_ngcc_current_state = 48;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 301:
/* 1714 */         this.blockValue = (Integer)$__result__;
/* 1715 */         this.$_ngcc_current_state = 81;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 258:
/* 1720 */         this.facet = (XSFacet)$__result__;
/* 1721 */         action12();
/* 1722 */         this.$_ngcc_current_state = 48;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 219:
/* 1727 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1728 */         action5();
/* 1729 */         this.$_ngcc_current_state = 17;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 232:
/* 1734 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1735 */         this.$_ngcc_current_state = 26;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 212:
/* 1740 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1741 */         this.$_ngcc_current_state = 10;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 242:
/* 1746 */         this.$_ngcc_current_state = 36;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 267:
/* 1751 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1752 */         this.$_ngcc_current_state = 54;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 210:
/* 1757 */         this.annotation = (AnnotationImpl)$__result__;
/* 1758 */         this.$_ngcc_current_state = 9;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 247:
/* 1763 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1764 */         this.$_ngcc_current_state = 39;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 205:
/* 1769 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1770 */         action0();
/* 1771 */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 262:
/* 1776 */         this.baseContentType = (Ref.SimpleType)$__result__;
/* 1777 */         this.$_ngcc_current_state = 51;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 225:
/* 1782 */         this.baseTypeName = (UName)$__result__;
/* 1783 */         action6();
/* 1784 */         this.$_ngcc_current_state = 22;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 208:
/* 1789 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1790 */         action2();
/* 1791 */         this.$_ngcc_current_state = 8;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 249:
/* 1796 */         this.baseTypeName = (UName)$__result__;
/* 1797 */         action9();
/* 1798 */         this.$_ngcc_current_state = 42;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 296:
/* 1803 */         this.finalValue = (Integer)$__result__;
/* 1804 */         this.$_ngcc_current_state = 77;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 284:
/* 1809 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1810 */         this.$_ngcc_current_state = 65;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 274:
/* 1815 */         this.annotation = (AnnotationImpl)$__result__;
/* 1816 */         this.$_ngcc_current_state = 35;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 214:
/* 1821 */         this.baseTypeName = (UName)$__result__;
/* 1822 */         action3();
/* 1823 */         this.$_ngcc_current_state = 13;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 254:
/* 1828 */         this.$_ngcc_current_state = 46;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 269:
/* 1833 */         this.baseTypeName = (UName)$__result__;
/* 1834 */         action14();
/* 1835 */         this.$_ngcc_current_state = 57;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 230:
/* 1840 */         this.annotation = (AnnotationImpl)$__result__;
/* 1841 */         this.$_ngcc_current_state = 7;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 282:
/* 1846 */         this.annotation = (AnnotationImpl)$__result__;
/* 1847 */         this.$_ngcc_current_state = 2;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 265:
/* 1852 */         this.annotation = (AnnotationImpl)$__result__;
/* 1853 */         this.$_ngcc_current_state = 52;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 223:
/* 1858 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1859 */         this.$_ngcc_current_state = 19;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 245:
/* 1864 */         this.annotation = (AnnotationImpl)$__result__;
/* 1865 */         this.$_ngcc_current_state = 38;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 276:
/* 1870 */         this.fa = (ForeignAttributesImpl)$__result__;
/* 1871 */         this.$_ngcc_current_state = 61;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 221:
/* 1876 */         this.annotation = (AnnotationImpl)$__result__;
/* 1877 */         this.$_ngcc_current_state = 18;
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean accepted() {
/* 1884 */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class BaseContentSimpleTypeRef
/*      */     implements Ref.SimpleType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private BaseContentSimpleTypeRef(Ref.Type _baseType) {
/* 1900 */       this.baseType = _baseType;
/*      */     } public XSSimpleType getType() {
/* 1902 */       return (XSSimpleType)((XSComplexType)this.baseType.getType()).getContentType();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeResult(int derivationMethod) {
/* 1910 */     if (this.finalValue == null)
/* 1911 */       this.finalValue = Integer.valueOf(this.$runtime.finalDefault); 
/* 1912 */     if (this.blockValue == null) {
/* 1913 */       this.blockValue = Integer.valueOf(this.$runtime.blockDefault);
/*      */     }
/* 1915 */     this.result = new ComplexTypeImpl(this.$runtime.document, this.annotation, this.locator, this.fa, this.name, (this.name == null), this.$runtime.parseBoolean(this.abstractValue), derivationMethod, this.baseType, this.finalValue.intValue(), this.blockValue.intValue(), this.$runtime.parseBoolean(this.mixedValue));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class BaseComplexTypeContentRef
/*      */     implements Ref.ContentType
/*      */   {
/*      */     private final Ref.Type baseType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private BaseComplexTypeContentRef(Ref.Type _baseType) {
/* 1936 */       this.baseType = _baseType;
/*      */     } public XSContentType getContentType() {
/* 1938 */       return ((XSComplexType)this.baseType.getType()).getContentType();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class InheritBaseContentTypeRef implements Ref.ContentType {
/*      */     private final Ref.Type baseType;
/*      */     private final XSContentType empty;
/*      */     private final XSContentType expContent;
/*      */     private final SchemaDocumentImpl currentDocument;
/*      */     
/*      */     private InheritBaseContentTypeRef(Ref.Type _baseType, XSContentType _explicitContent, NGCCRuntimeEx $runtime) {
/* 1949 */       this.baseType = _baseType;
/* 1950 */       this.currentDocument = $runtime.document;
/* 1951 */       this.expContent = _explicitContent;
/* 1952 */       this.empty = (XSContentType)$runtime.parser.schemaSet.empty;
/*      */     }
/*      */     public XSContentType getContentType() {
/* 1955 */       XSContentType baseContentType = ((XSComplexType)this.baseType.getType()).getContentType();
/*      */       
/* 1957 */       if (baseContentType == this.empty) {
/* 1958 */         return this.expContent;
/*      */       }
/* 1960 */       return (XSContentType)new ParticleImpl(this.currentDocument, null, (Ref.Term)new ModelGroupImpl(this.currentDocument, null, null, null, XSModelGroup.SEQUENCE, new ParticleImpl[] { (ParticleImpl)baseContentType, (ParticleImpl)this.expContent }), null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Ref.ContentType buildComplexExtensionContentModel(XSContentType explicitContent) {
/* 1971 */     if (explicitContent == this.$runtime.parser.schemaSet.empty) {
/* 1972 */       return new BaseComplexTypeContentRef(this.baseType);
/*      */     }
/* 1974 */     return new InheritBaseContentTypeRef(this.baseType, explicitContent, this.$runtime);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\complexType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
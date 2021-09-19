/*      */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*      */ import com.sun.xml.xsom.XSContentType;
/*      */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.xsom.impl.AttributesHolder;
/*      */ import com.sun.xml.xsom.impl.ContentTypeImpl;
/*      */ import com.sun.xml.xsom.impl.Ref;
/*      */ import com.sun.xml.xsom.impl.UName;
/*      */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*      */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*      */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*      */ import com.sun.xml.xsom.impl.parser.state.attributeUses;
/*      */ import com.sun.xml.xsom.impl.parser.state.complexType_complexContent_body;
/*      */ import com.sun.xml.xsom.impl.parser.state.erSet;
/*      */ import com.sun.xml.xsom.impl.parser.state.facet;
/*      */ import com.sun.xml.xsom.impl.parser.state.qname;
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
/*      */   private AnnotationImpl annotation;
/*      */   private ContentTypeImpl explicitContent;
/*      */   private UName baseTypeName;
/*      */   private String mixedValue;
/*      */   protected final NGCCRuntimeEx $runtime;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   35 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private ComplexTypeImpl result; private Ref.Type baseType; private Ref.ContentType contentType; private Ref.SimpleType baseContentType; private RestrictionSimpleTypeImpl contentSimpleType; private Locator locator; private Locator locator2;
/*      */   public complexType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*   39 */     super(source, parent, cookie);
/*   40 */     this.$runtime = runtime;
/*   41 */     this.$_ngcc_current_state = 81;
/*      */   }
/*      */   
/*      */   public complexType(NGCCRuntimeEx runtime) {
/*   45 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action0() throws SAXException {
/*   50 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action1() throws SAXException {
/*   56 */     this.baseType = (Ref.Type)this.$runtime.parser.schemaSet.anyType;
/*   57 */     makeResult(2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action2() throws SAXException {
/*   63 */     this.result.setExplicitContent((XSContentType)this.explicitContent);
/*   64 */     this.result.setContentType(buildComplexExtensionContentModel((XSContentType)this.explicitContent));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action3() throws SAXException {
/*   71 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*   73 */     makeResult(1);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action4() throws SAXException {
/*   78 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action5() throws SAXException {
/*   83 */     this.result.setContentType((Ref.ContentType)this.explicitContent);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action6() throws SAXException {
/*   89 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */     
/*   91 */     makeResult(2);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action7() throws SAXException {
/*   96 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action8() throws SAXException {
/*  101 */     this.contentType = (Ref.ContentType)new BaseContentRef(this.baseType, null);
/*  102 */     makeResult(1);
/*  103 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action9() throws SAXException {
/*  109 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action10() throws SAXException {
/*  115 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */ 
/*      */   
/*      */   private void action11() throws SAXException {
/*  120 */     makeResult(2);
/*  121 */     this.result.setContentType(this.contentType);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action12() throws SAXException {
/*  126 */     this.contentSimpleType.addFacet(this.facet);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action13() throws SAXException {
/*  131 */     if (this.baseContentType == null)
/*      */     {
/*  133 */       this.baseContentType = (Ref.SimpleType)new BaseContentSimpleTypeRef(this.baseType, null);
/*      */     }
/*      */     
/*  136 */     this.contentSimpleType = new RestrictionSimpleTypeImpl(this.$runtime.currentSchema, null, this.locator2, null, true, this.baseContentType);
/*      */ 
/*      */     
/*  139 */     this.contentType = (Ref.ContentType)this.contentSimpleType;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action14() throws SAXException {
/*  145 */     this.baseType = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator2, this.$runtime.currentSchema, this.baseTypeName);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action15() throws SAXException {
/*  151 */     this.locator2 = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   private void action16() throws SAXException {
/*  155 */     this.locator = this.$runtime.copyLocator();
/*      */   }
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*  160 */     this.$uri = $__uri;
/*  161 */     this.$localName = $__local;
/*  162 */     this.$qname = $__qname;
/*  163 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 7:
/*  166 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  167 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  168 */           action7();
/*  169 */           this.$_ngcc_current_state = 22;
/*      */         
/*      */         }
/*  172 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  173 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  174 */           action4();
/*  175 */           this.$_ngcc_current_state = 14;
/*      */         } else {
/*      */           
/*  178 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 47:
/*  185 */         action13();
/*  186 */         this.$_ngcc_current_state = 45;
/*  187 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 32:
/*  192 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  193 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  194 */           action15();
/*  195 */           this.$_ngcc_current_state = 54;
/*      */         
/*      */         }
/*  198 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  199 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  200 */           action10();
/*  201 */           this.$_ngcc_current_state = 40;
/*      */         } else {
/*      */           
/*  204 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 40:
/*  211 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  212 */           this.$runtime.consumeAttribute($ai);
/*  213 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  216 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/*  222 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  223 */           this.$runtime.consumeAttribute($ai);
/*  224 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  227 */           this.$_ngcc_current_state = 61;
/*  228 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  234 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/*  239 */         action8();
/*  240 */         this.$_ngcc_current_state = 34;
/*  241 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/*  246 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  247 */           this.$runtime.consumeAttribute($ai);
/*  248 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  251 */           this.$_ngcc_current_state = 73;
/*  252 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/*  258 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  259 */           this.$runtime.consumeAttribute($ai);
/*  260 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  263 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  269 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  270 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 197);
/*  271 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  274 */           this.$_ngcc_current_state = 47;
/*  275 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  281 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  282 */           annotation annotation = new annotation(this, this._source, this.$runtime, 181, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  283 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  286 */           this.$_ngcc_current_state = 35;
/*  287 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  293 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  294 */           annotation annotation = new annotation(this, this._source, this.$runtime, 159, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  295 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  298 */           this.$_ngcc_current_state = 17;
/*  299 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  305 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  306 */           annotation annotation = new annotation(this, this._source, this.$runtime, 208, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  307 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  310 */           this.$_ngcc_current_state = 32;
/*  311 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  317 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  318 */           facet facet = new facet(this, this._source, this.$runtime, 192);
/*  319 */           spawnChildFromEnterElement((NGCCEventReceiver)facet, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  322 */           action11();
/*  323 */           this.$_ngcc_current_state = 43;
/*  324 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  330 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  331 */           annotation annotation = new annotation(this, this._source, this.$runtime, 215, null, AnnotationContext.COMPLEXTYPE_DECL);
/*  332 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  335 */           this.$_ngcc_current_state = 2;
/*  336 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 81:
/*  342 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  343 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  344 */           action16();
/*  345 */           this.$_ngcc_current_state = 77;
/*      */         } else {
/*      */           
/*  348 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  354 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  355 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 147, (AttributesHolder)this.result);
/*  356 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  359 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 50:
/*  365 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  366 */           annotation annotation = new annotation(this, this._source, this.$runtime, 200, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  367 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  370 */           this.$_ngcc_current_state = 48;
/*  371 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/*  377 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  378 */           this.$runtime.consumeAttribute($ai);
/*  379 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  382 */           this.$_ngcc_current_state = 65;
/*  383 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  389 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  390 */           this.$runtime.consumeAttribute($ai);
/*  391 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  394 */           this.$_ngcc_current_state = 24;
/*  395 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  401 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  402 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  403 */           this.$_ngcc_current_state = 56;
/*      */         
/*      */         }
/*  406 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  407 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  408 */           this.$_ngcc_current_state = 26;
/*      */         
/*      */         }
/*  411 */         else if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  412 */           action1();
/*  413 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 144, (AttributesHolder)this.result);
/*  414 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  417 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 45:
/*  425 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  426 */           facet facet = new facet(this, this._source, this.$runtime, 193);
/*  427 */           spawnChildFromEnterElement((NGCCEventReceiver)facet, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  430 */           this.$_ngcc_current_state = 44;
/*  431 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  437 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  438 */           annotation annotation = new annotation(this, this._source, this.$runtime, 149, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  439 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  442 */           this.$_ngcc_current_state = 9;
/*  443 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/*  449 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  450 */           this.$runtime.consumeAttribute($ai);
/*  451 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  454 */           this.$_ngcc_current_state = 69;
/*  455 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 43:
/*  461 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  462 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 189, (AttributesHolder)this.result);
/*  463 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  466 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  472 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  473 */           annotation annotation = new annotation(this, this._source, this.$runtime, 167, this.annotation, AnnotationContext.COMPLEXTYPE_DECL);
/*  474 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  477 */           this.$_ngcc_current_state = 7;
/*  478 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 14:
/*  484 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  485 */           this.$runtime.consumeAttribute($ai);
/*  486 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  489 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 22:
/*  495 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  496 */           this.$runtime.consumeAttribute($ai);
/*  497 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  500 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/*  506 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  507 */           this.$runtime.consumeAttribute($ai);
/*  508 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  511 */           this.$_ngcc_current_state = 59;
/*  512 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  518 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  519 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 157, (AttributesHolder)this.result);
/*  520 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  523 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 34:
/*  529 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/*  530 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 178, (AttributesHolder)this.result);
/*  531 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  534 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  540 */     unexpectedEnterElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*  548 */     this.$uri = $__uri;
/*  549 */     this.$localName = $__local;
/*  550 */     this.$qname = $__qname;
/*  551 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 16:
/*  554 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  555 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  556 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  559 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  565 */         action13();
/*  566 */         this.$_ngcc_current_state = 45;
/*  567 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  572 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  573 */           this.$runtime.consumeAttribute($ai);
/*  574 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  577 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/*  583 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  584 */           this.$runtime.consumeAttribute($ai);
/*  585 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  588 */           this.$_ngcc_current_state = 61;
/*  589 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  595 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  600 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  601 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  602 */           this.$_ngcc_current_state = 0;
/*      */         } else {
/*      */           
/*  605 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/*  611 */         action8();
/*  612 */         this.$_ngcc_current_state = 34;
/*  613 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/*  618 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  619 */           this.$runtime.consumeAttribute($ai);
/*  620 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  623 */           this.$_ngcc_current_state = 73;
/*  624 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/*  630 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  631 */           this.$runtime.consumeAttribute($ai);
/*  632 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  635 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 6:
/*  641 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexContent")) {
/*  642 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  643 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  646 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/*  652 */         this.$_ngcc_current_state = 47;
/*  653 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 8:
/*  658 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  659 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  660 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  663 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  669 */         this.$_ngcc_current_state = 17;
/*  670 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  675 */         this.$_ngcc_current_state = 35;
/*  676 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/*  681 */         this.$_ngcc_current_state = 32;
/*  682 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  687 */         action11();
/*  688 */         this.$_ngcc_current_state = 43;
/*  689 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  694 */         this.$_ngcc_current_state = 2;
/*  695 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 9:
/*  700 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  701 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 147, (AttributesHolder)this.result);
/*  702 */           spawnChildFromLeaveElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  705 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 50:
/*  711 */         this.$_ngcc_current_state = 48;
/*  712 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 33:
/*  717 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  718 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  719 */           this.$_ngcc_current_state = 31;
/*      */         } else {
/*      */           
/*  722 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/*  728 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  729 */           this.$runtime.consumeAttribute($ai);
/*  730 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  733 */           this.$_ngcc_current_state = 65;
/*  734 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  740 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/*  741 */           this.$runtime.consumeAttribute($ai);
/*  742 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  745 */           this.$_ngcc_current_state = 24;
/*  746 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 31:
/*  752 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleContent")) {
/*  753 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  754 */           this.$_ngcc_current_state = 1;
/*      */         } else {
/*      */           
/*  757 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 2:
/*  763 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*  764 */           action1();
/*  765 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 144, (AttributesHolder)this.result);
/*  766 */           spawnChildFromLeaveElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  769 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 45:
/*  775 */         this.$_ngcc_current_state = 44;
/*  776 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 42:
/*  781 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  782 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  783 */           this.$_ngcc_current_state = 31;
/*      */         } else {
/*      */           
/*  786 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/*  792 */         this.$_ngcc_current_state = 9;
/*  793 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/*  798 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  799 */           this.$runtime.consumeAttribute($ai);
/*  800 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  803 */           this.$_ngcc_current_state = 69;
/*  804 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 43:
/*  810 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  811 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 189, (AttributesHolder)this.result);
/*  812 */           spawnChildFromLeaveElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  815 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  821 */         this.$_ngcc_current_state = 7;
/*  822 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 14:
/*  827 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  828 */           this.$runtime.consumeAttribute($ai);
/*  829 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  832 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 22:
/*  838 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/*  839 */           this.$runtime.consumeAttribute($ai);
/*  840 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  843 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/*  849 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  850 */           this.$runtime.consumeAttribute($ai);
/*  851 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  854 */           this.$_ngcc_current_state = 59;
/*  855 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  861 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  862 */           complexType_complexContent_body complexType_complexContent_body = new complexType_complexContent_body(this, this._source, this.$runtime, 157, (AttributesHolder)this.result);
/*  863 */           spawnChildFromLeaveElement((NGCCEventReceiver)complexType_complexContent_body, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  866 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 34:
/*  872 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("extension")) {
/*  873 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 178, (AttributesHolder)this.result);
/*  874 */           spawnChildFromLeaveElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  877 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  883 */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  891 */     this.$uri = $__uri;
/*  892 */     this.$localName = $__local;
/*  893 */     this.$qname = $__qname;
/*  894 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 44:
/*  897 */         action11();
/*  898 */         this.$_ngcc_current_state = 43;
/*  899 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 47:
/*  904 */         action13();
/*  905 */         this.$_ngcc_current_state = 45;
/*  906 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/*  911 */         this.$_ngcc_current_state = 2;
/*  912 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  917 */         if ($__uri.equals("") && $__local.equals("base")) {
/*  918 */           this.$_ngcc_current_state = 39;
/*      */         } else {
/*      */           
/*  921 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/*  927 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/*  928 */           this.$_ngcc_current_state = 67;
/*      */         } else {
/*      */           
/*  931 */           this.$_ngcc_current_state = 61;
/*  932 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 50:
/*  938 */         this.$_ngcc_current_state = 48;
/*  939 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/*  944 */         if ($__uri.equals("") && $__local.equals("final")) {
/*  945 */           this.$_ngcc_current_state = 71;
/*      */         } else {
/*      */           
/*  948 */           this.$_ngcc_current_state = 65;
/*  949 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  955 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/*  960 */         action8();
/*  961 */         this.$_ngcc_current_state = 34;
/*  962 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/*  967 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/*  968 */           this.$_ngcc_current_state = 28;
/*      */         } else {
/*      */           
/*  971 */           this.$_ngcc_current_state = 24;
/*  972 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/*  978 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/*  979 */           this.$_ngcc_current_state = 79;
/*      */         } else {
/*      */           
/*  982 */           this.$_ngcc_current_state = 73;
/*  983 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 54:
/*  989 */         if ($__uri.equals("") && $__local.equals("base")) {
/*  990 */           this.$_ngcc_current_state = 53;
/*      */         } else {
/*      */           
/*  993 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 45:
/*  999 */         this.$_ngcc_current_state = 44;
/* 1000 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1005 */         this.$_ngcc_current_state = 9;
/* 1006 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/* 1011 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1012 */           this.$_ngcc_current_state = 75;
/*      */         } else {
/*      */           
/* 1015 */           this.$_ngcc_current_state = 69;
/* 1016 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1022 */         this.$_ngcc_current_state = 47;
/* 1023 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/* 1028 */         this.$_ngcc_current_state = 17;
/* 1029 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/* 1034 */         this.$_ngcc_current_state = 35;
/* 1035 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/* 1040 */         this.$_ngcc_current_state = 7;
/* 1041 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 14:
/* 1046 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1047 */           this.$_ngcc_current_state = 13;
/*      */         } else {
/*      */           
/* 1050 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 22:
/* 1056 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1057 */           this.$_ngcc_current_state = 21;
/*      */         } else {
/*      */           
/* 1060 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1066 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1067 */           this.$_ngcc_current_state = 63;
/*      */         } else {
/*      */           
/* 1070 */           this.$_ngcc_current_state = 59;
/* 1071 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/* 1077 */         this.$_ngcc_current_state = 32;
/* 1078 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1083 */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException
/*      */   {
/* 1091 */     this.$uri = $__uri;
/* 1092 */     this.$localName = $__local;
/* 1093 */     this.$qname = $__qname;
/* 1094 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 47:
/* 1097 */         action13();
/* 1098 */         this.$_ngcc_current_state = 45;
/* 1099 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 65:
/* 1104 */         this.$_ngcc_current_state = 61;
/* 1105 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/* 1110 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 35:
/* 1115 */         action8();
/* 1116 */         this.$_ngcc_current_state = 34;
/* 1117 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 70:
/* 1122 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 1123 */           this.$_ngcc_current_state = 65;
/*      */         } else {
/*      */           
/* 1126 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 77:
/* 1132 */         this.$_ngcc_current_state = 73;
/* 1133 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 62:
/* 1138 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 1139 */           this.$_ngcc_current_state = 59;
/*      */         } else {
/*      */           
/* 1142 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 66:
/* 1148 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1149 */           this.$_ngcc_current_state = 61;
/*      */         } else {
/*      */           
/* 1152 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 48:
/* 1158 */         this.$_ngcc_current_state = 47;
/* 1159 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 12:
/* 1164 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1165 */           this.$_ngcc_current_state = 10;
/*      */         } else {
/*      */           
/* 1168 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 38:
/* 1174 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1175 */           this.$_ngcc_current_state = 36;
/*      */         } else {
/*      */           
/* 1178 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/* 1184 */         this.$_ngcc_current_state = 35;
/* 1185 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/* 1190 */         this.$_ngcc_current_state = 17;
/* 1191 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 20:
/* 1196 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1197 */           this.$_ngcc_current_state = 18;
/*      */         } else {
/*      */           
/* 1200 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 56:
/* 1206 */         this.$_ngcc_current_state = 32;
/* 1207 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/* 1212 */         action11();
/* 1213 */         this.$_ngcc_current_state = 43;
/* 1214 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 59:
/* 1219 */         this.$_ngcc_current_state = 2;
/* 1220 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 27:
/* 1225 */         if ($__uri.equals("") && $__local.equals("mixed")) {
/* 1226 */           this.$_ngcc_current_state = 24;
/*      */         } else {
/*      */           
/* 1229 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 50:
/* 1235 */         this.$_ngcc_current_state = 48;
/* 1236 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 69:
/* 1241 */         this.$_ngcc_current_state = 65;
/* 1242 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 52:
/* 1247 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 1248 */           this.$_ngcc_current_state = 50;
/*      */         } else {
/*      */           
/* 1251 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 26:
/* 1257 */         this.$_ngcc_current_state = 24;
/* 1258 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 74:
/* 1263 */         if ($__uri.equals("") && $__local.equals("block")) {
/* 1264 */           this.$_ngcc_current_state = 69;
/*      */         } else {
/*      */           
/* 1267 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 45:
/* 1273 */         this.$_ngcc_current_state = 44;
/* 1274 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 10:
/* 1279 */         this.$_ngcc_current_state = 9;
/* 1280 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 73:
/* 1285 */         this.$_ngcc_current_state = 69;
/* 1286 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/* 1291 */         this.$_ngcc_current_state = 7;
/* 1292 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 78:
/* 1297 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/* 1298 */           this.$_ngcc_current_state = 73;
/*      */         } else {
/*      */           
/* 1301 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 61:
/* 1307 */         this.$_ngcc_current_state = 59;
/* 1308 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1313 */     unexpectedLeaveAttribute($__qname); } public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     qname qname4;
/*      */     qname qname3;
/*      */     erSet erSet2;
/*      */     qname qname2;
/*      */     qname qname1;
/*      */     erSet erSet1;
/* 1321 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 47:
/* 1324 */         action13();
/* 1325 */         this.$_ngcc_current_state = 45;
/* 1326 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 39:
/* 1331 */         qname4 = new qname(this, this._source, this.$runtime, 184);
/* 1332 */         spawnChildFromText((NGCCEventReceiver)qname4, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 40:
/* 1337 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1338 */           this.$runtime.consumeAttribute($ai);
/* 1339 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 65:
/* 1345 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1346 */           this.$runtime.consumeAttribute($ai);
/* 1347 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1350 */         this.$_ngcc_current_state = 61;
/* 1351 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 79:
/* 1357 */         this.abstractValue = $value;
/* 1358 */         this.$_ngcc_current_state = 78;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 0:
/* 1363 */         revertToParentFromText(this.result, this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 35:
/* 1368 */         action8();
/* 1369 */         this.$_ngcc_current_state = 34;
/* 1370 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 53:
/* 1375 */         qname3 = new qname(this, this._source, this.$runtime, 203);
/* 1376 */         spawnChildFromText((NGCCEventReceiver)qname3, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 77:
/* 1381 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/* 1382 */           this.$runtime.consumeAttribute($ai);
/* 1383 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1386 */         this.$_ngcc_current_state = 73;
/* 1387 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 54:
/* 1393 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1394 */           this.$runtime.consumeAttribute($ai);
/* 1395 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 71:
/* 1401 */         erSet2 = new erSet(this, this._source, this.$runtime, 228);
/* 1402 */         spawnChildFromText((NGCCEventReceiver)erSet2, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 13:
/* 1407 */         qname2 = new qname(this, this._source, this.$runtime, 152);
/* 1408 */         spawnChildFromText((NGCCEventReceiver)qname2, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 48:
/* 1413 */         this.$_ngcc_current_state = 47;
/* 1414 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 36:
/* 1419 */         this.$_ngcc_current_state = 35;
/* 1420 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 21:
/* 1425 */         qname1 = new qname(this, this._source, this.$runtime, 162);
/* 1426 */         spawnChildFromText((NGCCEventReceiver)qname1, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 18:
/* 1431 */         this.$_ngcc_current_state = 17;
/* 1432 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 75:
/* 1437 */         erSet1 = new erSet(this, this._source, this.$runtime, 233);
/* 1438 */         spawnChildFromText((NGCCEventReceiver)erSet1, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 56:
/* 1443 */         this.$_ngcc_current_state = 32;
/* 1444 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 63:
/* 1449 */         this.name = $value;
/* 1450 */         this.$_ngcc_current_state = 62;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 44:
/* 1455 */         action11();
/* 1456 */         this.$_ngcc_current_state = 43;
/* 1457 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 59:
/* 1462 */         this.$_ngcc_current_state = 2;
/* 1463 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 50:
/* 1468 */         this.$_ngcc_current_state = 48;
/* 1469 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 69:
/* 1474 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 1475 */           this.$runtime.consumeAttribute($ai);
/* 1476 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1479 */         this.$_ngcc_current_state = 65;
/* 1480 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 26:
/* 1486 */         if (($ai = this.$runtime.getAttributeIndex("", "mixed")) >= 0) {
/* 1487 */           this.$runtime.consumeAttribute($ai);
/* 1488 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1491 */         this.$_ngcc_current_state = 24;
/* 1492 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 45:
/* 1498 */         this.$_ngcc_current_state = 44;
/* 1499 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 10:
/* 1504 */         this.$_ngcc_current_state = 9;
/* 1505 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 73:
/* 1510 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/* 1511 */           this.$runtime.consumeAttribute($ai);
/* 1512 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1515 */         this.$_ngcc_current_state = 69;
/* 1516 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 28:
/* 1522 */         this.mixedValue = $value;
/* 1523 */         this.$_ngcc_current_state = 27;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 24:
/* 1528 */         this.$_ngcc_current_state = 7;
/* 1529 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 14:
/* 1534 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1535 */           this.$runtime.consumeAttribute($ai);
/* 1536 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 22:
/* 1542 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 1543 */           this.$runtime.consumeAttribute($ai);
/* 1544 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 67:
/* 1550 */         this.mixedValue = $value;
/* 1551 */         this.$_ngcc_current_state = 66;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 61:
/* 1556 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 1557 */           this.$runtime.consumeAttribute($ai);
/* 1558 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/* 1561 */         this.$_ngcc_current_state = 59;
/* 1562 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 1570 */     switch ($__cookie__) {
/*      */       
/*      */       case 203:
/* 1573 */         this.baseTypeName = (UName)$__result__;
/* 1574 */         action14();
/* 1575 */         this.$_ngcc_current_state = 52;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 228:
/* 1580 */         this.finalValue = (Integer)$__result__;
/* 1581 */         this.$_ngcc_current_state = 70;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 197:
/* 1586 */         this.baseContentType = (Ref.SimpleType)$__result__;
/* 1587 */         this.$_ngcc_current_state = 47;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 181:
/* 1592 */         this.annotation = (AnnotationImpl)$__result__;
/* 1593 */         this.$_ngcc_current_state = 35;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 192:
/* 1598 */         this.facet = (XSFacet)$__result__;
/* 1599 */         action12();
/* 1600 */         this.$_ngcc_current_state = 44;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 215:
/* 1605 */         this.annotation = (AnnotationImpl)$__result__;
/* 1606 */         this.$_ngcc_current_state = 2;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 200:
/* 1611 */         this.annotation = (AnnotationImpl)$__result__;
/* 1612 */         this.$_ngcc_current_state = 48;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 147:
/* 1617 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1618 */         action2();
/* 1619 */         this.$_ngcc_current_state = 8;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 189:
/* 1624 */         this.$_ngcc_current_state = 42;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 167:
/* 1629 */         this.annotation = (AnnotationImpl)$__result__;
/* 1630 */         this.$_ngcc_current_state = 7;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 178:
/* 1635 */         this.$_ngcc_current_state = 33;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 184:
/* 1640 */         this.baseTypeName = (UName)$__result__;
/* 1641 */         action9();
/* 1642 */         this.$_ngcc_current_state = 38;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 152:
/* 1647 */         this.baseTypeName = (UName)$__result__;
/* 1648 */         action3();
/* 1649 */         this.$_ngcc_current_state = 12;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 162:
/* 1654 */         this.baseTypeName = (UName)$__result__;
/* 1655 */         action6();
/* 1656 */         this.$_ngcc_current_state = 20;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 159:
/* 1661 */         this.annotation = (AnnotationImpl)$__result__;
/* 1662 */         this.$_ngcc_current_state = 17;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 233:
/* 1667 */         this.blockValue = (Integer)$__result__;
/* 1668 */         this.$_ngcc_current_state = 74;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 208:
/* 1673 */         this.annotation = (AnnotationImpl)$__result__;
/* 1674 */         this.$_ngcc_current_state = 32;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 144:
/* 1679 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1680 */         action0();
/* 1681 */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 193:
/* 1686 */         this.facet = (XSFacet)$__result__;
/* 1687 */         action12();
/* 1688 */         this.$_ngcc_current_state = 44;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 149:
/* 1693 */         this.annotation = (AnnotationImpl)$__result__;
/* 1694 */         this.$_ngcc_current_state = 9;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 157:
/* 1699 */         this.explicitContent = (ContentTypeImpl)$__result__;
/* 1700 */         action5();
/* 1701 */         this.$_ngcc_current_state = 16;
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean accepted() {
/* 1708 */     return (this.$_ngcc_current_state == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void makeResult(int derivationMethod) {
/* 1746 */     if (this.finalValue == null)
/* 1747 */       this.finalValue = new Integer(this.$runtime.finalDefault); 
/* 1748 */     if (this.blockValue == null) {
/* 1749 */       this.blockValue = new Integer(this.$runtime.blockDefault);
/*      */     }
/* 1751 */     this.result = new ComplexTypeImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.name, (this.name == null), this.$runtime.parseBoolean(this.abstractValue), derivationMethod, this.baseType, this.finalValue.intValue(), this.blockValue.intValue(), this.$runtime.parseBoolean(this.mixedValue));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Ref.ContentType buildComplexExtensionContentModel(XSContentType explicitContent) {
/* 1806 */     if (explicitContent == this.$runtime.parser.schemaSet.empty) {
/* 1807 */       return (Ref.ContentType)new BaseComplexTypeContentRef(this.baseType, null);
/*      */     }
/* 1809 */     return (Ref.ContentType)new InheritBaseContentTypeRef(this.baseType, explicitContent, this.$runtime, null);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\complexType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
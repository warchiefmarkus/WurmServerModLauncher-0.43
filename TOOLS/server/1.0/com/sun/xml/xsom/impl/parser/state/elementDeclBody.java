/*      */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*      */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*      */ import com.sun.xml.xsom.impl.ElementDecl;
/*      */ import com.sun.xml.xsom.impl.Ref;
/*      */ import com.sun.xml.xsom.impl.UName;
/*      */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*      */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*      */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*      */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*      */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*      */ import com.sun.xml.xsom.impl.parser.state.complexType;
/*      */ import com.sun.xml.xsom.impl.parser.state.erSet;
/*      */ import com.sun.xml.xsom.impl.parser.state.ersSet;
/*      */ import com.sun.xml.xsom.impl.parser.state.identityConstraint;
/*      */ import com.sun.xml.xsom.impl.parser.state.qname;
/*      */ import com.sun.xml.xsom.impl.parser.state.qualification;
/*      */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ class elementDeclBody extends NGCCHandler {
/*      */   private Integer finalValue;
/*      */   private String nillable;
/*      */   private String name;
/*      */   private String abstractValue;
/*      */   private Integer blockValue;
/*      */   private AnnotationImpl annotation;
/*      */   private Locator locator;
/*      */   private String defaultValue;
/*      */   private boolean isGlobal;
/*      */   private String fixedValue;
/*      */   
/*      */   public final NGCCRuntime getRuntime() {
/*   38 */     return (NGCCRuntime)this.$runtime;
/*      */   }
/*      */   private UName typeName; private UName substRef; protected final NGCCRuntimeEx $runtime; private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private boolean form; private boolean formSpecified; private Ref.Type type; private DelayedRef.Element substHeadRef;
/*      */   public elementDeclBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator, boolean _isGlobal) {
/*   42 */     super(source, parent, cookie);
/*   43 */     this.$runtime = runtime;
/*   44 */     this.locator = _locator;
/*   45 */     this.isGlobal = _isGlobal;
/*   46 */     this.$_ngcc_current_state = 44;
/*      */   }
/*      */   
/*      */   public elementDeclBody(NGCCRuntimeEx runtime, Locator _locator, boolean _isGlobal) {
/*   50 */     this(null, (NGCCEventSource)runtime, runtime, -1, _locator, _isGlobal);
/*      */   }
/*      */ 
/*      */   
/*      */   private void action0() throws SAXException {
/*   55 */     this.type = (Ref.Type)new DelayedRef.Type((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void action1() throws SAXException {
/*   62 */     this.substHeadRef = new DelayedRef.Element((PatcherManager)this.$runtime, this.locator, this.$runtime.currentSchema, this.substRef);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void action2() throws SAXException {
/*   68 */     this.formSpecified = true;
/*      */   }
/*      */   
/*      */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*      */     int $ai;
/*   73 */     this.$uri = $__uri;
/*   74 */     this.$localName = $__local;
/*   75 */     this.$qname = $__qname;
/*   76 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 3:
/*   79 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*   80 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 325);
/*   81 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*      */         
/*      */         }
/*   84 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("complexType")) {
/*   85 */           complexType complexType = new complexType(this, this._source, this.$runtime, 326);
/*   86 */           spawnChildFromEnterElement((NGCCEventReceiver)complexType, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           int i;
/*   89 */           if ((i = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*   90 */             this.$runtime.consumeAttribute(i);
/*   91 */             this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */           } else {
/*      */             
/*   94 */             this.$_ngcc_current_state = 1;
/*   95 */             this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */           } 
/*      */         } 
/*      */         return;
/*      */ 
/*      */ 
/*      */       
/*      */       case 23:
/*  103 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  104 */           this.$runtime.consumeAttribute($ai);
/*  105 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  108 */           unexpectedEnterElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  114 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/*  115 */           identityConstraint identityConstraint = new identityConstraint(this, this._source, this.$runtime, 313);
/*  116 */           spawnChildFromEnterElement((NGCCEventReceiver)identityConstraint, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  119 */           this.$_ngcc_current_state = 0;
/*  120 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  126 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  127 */           annotation annotation = new annotation(this, this._source, this.$runtime, 330, null, AnnotationContext.ELEMENT_DECL);
/*  128 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  131 */           this.$_ngcc_current_state = 3;
/*  132 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 32:
/*  138 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  139 */           this.$runtime.consumeAttribute($ai);
/*  140 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  143 */           this.$_ngcc_current_state = 28;
/*  144 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  150 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/*  151 */           identityConstraint identityConstraint = new identityConstraint(this, this._source, this.$runtime, 312);
/*  152 */           spawnChildFromEnterElement((NGCCEventReceiver)identityConstraint, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  155 */           revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  161 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  162 */           this.$runtime.consumeAttribute($ai);
/*  163 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  166 */           this.$_ngcc_current_state = 24;
/*  167 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  173 */         if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  174 */           this.$runtime.consumeAttribute($ai);
/*  175 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  178 */           this.$_ngcc_current_state = 13;
/*  179 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  185 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  186 */           this.$runtime.consumeAttribute($ai);
/*  187 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  190 */           this.$_ngcc_current_state = 23;
/*  191 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  197 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  198 */           this.$runtime.consumeAttribute($ai);
/*  199 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  202 */           this.$_ngcc_current_state = 36;
/*  203 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  209 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  210 */           this.$runtime.consumeAttribute($ai);
/*  211 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  214 */           this.$_ngcc_current_state = 32;
/*  215 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/*  221 */         if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  222 */           this.$runtime.consumeAttribute($ai);
/*  223 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  226 */           this.$_ngcc_current_state = 11;
/*  227 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  233 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  234 */           this.$runtime.consumeAttribute($ai);
/*  235 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } else {
/*      */           
/*  238 */           this.$_ngcc_current_state = 40;
/*  239 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  245 */     unexpectedEnterElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*      */     int $ai;
/*  253 */     this.$uri = $__uri;
/*  254 */     this.$localName = $__local;
/*  255 */     this.$qname = $__qname;
/*  256 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 3:
/*  259 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  260 */           this.$runtime.consumeAttribute($ai);
/*  261 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  264 */           this.$_ngcc_current_state = 1;
/*  265 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 23:
/*  271 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  272 */           this.$runtime.consumeAttribute($ai);
/*  273 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  276 */           unexpectedLeaveElement($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  282 */         this.$_ngcc_current_state = 0;
/*  283 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  288 */         this.$_ngcc_current_state = 3;
/*  289 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 32:
/*  294 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  295 */           this.$runtime.consumeAttribute($ai);
/*  296 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  299 */           this.$_ngcc_current_state = 28;
/*  300 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  306 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  311 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  312 */           this.$runtime.consumeAttribute($ai);
/*  313 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  316 */           this.$_ngcc_current_state = 24;
/*  317 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  323 */         if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  324 */           this.$runtime.consumeAttribute($ai);
/*  325 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  328 */           this.$_ngcc_current_state = 13;
/*  329 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  335 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  336 */           this.$runtime.consumeAttribute($ai);
/*  337 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  340 */           this.$_ngcc_current_state = 23;
/*  341 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  347 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  348 */           this.$runtime.consumeAttribute($ai);
/*  349 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  352 */           this.$_ngcc_current_state = 36;
/*  353 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  359 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  360 */           this.$runtime.consumeAttribute($ai);
/*  361 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  364 */           this.$_ngcc_current_state = 32;
/*  365 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/*  371 */         if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  372 */           this.$runtime.consumeAttribute($ai);
/*  373 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  376 */           this.$_ngcc_current_state = 11;
/*  377 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  383 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  384 */           this.$runtime.consumeAttribute($ai);
/*  385 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } else {
/*      */           
/*  388 */           this.$_ngcc_current_state = 40;
/*  389 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  395 */     unexpectedLeaveElement($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  403 */     this.$uri = $__uri;
/*  404 */     this.$localName = $__local;
/*  405 */     this.$qname = $__qname;
/*  406 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 3:
/*  409 */         if ($__uri.equals("") && $__local.equals("type")) {
/*  410 */           this.$_ngcc_current_state = 6;
/*      */         } else {
/*      */           
/*  413 */           this.$_ngcc_current_state = 1;
/*  414 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 23:
/*  420 */         if ($__uri.equals("") && $__local.equals("name")) {
/*  421 */           this.$_ngcc_current_state = 22;
/*      */         } else {
/*      */           
/*  424 */           unexpectedEnterAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  430 */         this.$_ngcc_current_state = 0;
/*  431 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  436 */         this.$_ngcc_current_state = 3;
/*  437 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 32:
/*  442 */         if ($__uri.equals("") && $__local.equals("default")) {
/*  443 */           this.$_ngcc_current_state = 34;
/*      */         } else {
/*      */           
/*  446 */           this.$_ngcc_current_state = 28;
/*  447 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  453 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  458 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*  459 */           this.$_ngcc_current_state = 30;
/*      */         } else {
/*      */           
/*  462 */           this.$_ngcc_current_state = 24;
/*  463 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  469 */         if ($__uri.equals("") && $__local.equals("nillable")) {
/*  470 */           this.$_ngcc_current_state = 19;
/*      */         } else {
/*      */           
/*  473 */           this.$_ngcc_current_state = 13;
/*  474 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  480 */         if ($__uri.equals("") && $__local.equals("form")) {
/*  481 */           this.$_ngcc_current_state = 26;
/*      */         } else {
/*      */           
/*  484 */           this.$_ngcc_current_state = 23;
/*  485 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  491 */         if ($__uri.equals("") && $__local.equals("block")) {
/*  492 */           this.$_ngcc_current_state = 42;
/*      */         } else {
/*      */           
/*  495 */           this.$_ngcc_current_state = 36;
/*  496 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  502 */         if ($__uri.equals("") && $__local.equals("final")) {
/*  503 */           this.$_ngcc_current_state = 38;
/*      */         } else {
/*      */           
/*  506 */           this.$_ngcc_current_state = 32;
/*  507 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/*  513 */         if ($__uri.equals("") && $__local.equals("substitutionGroup")) {
/*  514 */           this.$_ngcc_current_state = 15;
/*      */         } else {
/*      */           
/*  517 */           this.$_ngcc_current_state = 11;
/*  518 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  524 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/*  525 */           this.$_ngcc_current_state = 46;
/*      */         } else {
/*      */           
/*  528 */           this.$_ngcc_current_state = 40;
/*  529 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  535 */     unexpectedEnterAttribute($__qname);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  543 */     this.$uri = $__uri;
/*  544 */     this.$localName = $__local;
/*  545 */     this.$qname = $__qname;
/*  546 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 3:
/*  549 */         this.$_ngcc_current_state = 1;
/*  550 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 1:
/*  555 */         this.$_ngcc_current_state = 0;
/*  556 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 11:
/*  561 */         this.$_ngcc_current_state = 3;
/*  562 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 14:
/*  567 */         if ($__uri.equals("") && $__local.equals("substitutionGroup")) {
/*  568 */           this.$_ngcc_current_state = 11;
/*      */         } else {
/*      */           
/*  571 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 32:
/*  577 */         this.$_ngcc_current_state = 28;
/*  578 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 0:
/*  583 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 28:
/*  588 */         this.$_ngcc_current_state = 24;
/*  589 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 33:
/*  594 */         if ($__uri.equals("") && $__local.equals("default")) {
/*  595 */           this.$_ngcc_current_state = 28;
/*      */         } else {
/*      */           
/*  598 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 24:
/*  604 */         this.$_ngcc_current_state = 23;
/*  605 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 17:
/*  610 */         this.$_ngcc_current_state = 13;
/*  611 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 41:
/*  616 */         if ($__uri.equals("") && $__local.equals("block")) {
/*  617 */           this.$_ngcc_current_state = 36;
/*      */         } else {
/*      */           
/*  620 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 36:
/*  626 */         this.$_ngcc_current_state = 32;
/*  627 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 40:
/*  632 */         this.$_ngcc_current_state = 36;
/*  633 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 29:
/*  638 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/*  639 */           this.$_ngcc_current_state = 24;
/*      */         } else {
/*      */           
/*  642 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 25:
/*  648 */         if ($__uri.equals("") && $__local.equals("form")) {
/*  649 */           this.$_ngcc_current_state = 23;
/*      */         } else {
/*      */           
/*  652 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 13:
/*  658 */         this.$_ngcc_current_state = 11;
/*  659 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 21:
/*  664 */         if ($__uri.equals("") && $__local.equals("name")) {
/*  665 */           this.$_ngcc_current_state = 17;
/*      */         } else {
/*      */           
/*  668 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 5:
/*  674 */         if ($__uri.equals("") && $__local.equals("type")) {
/*  675 */           this.$_ngcc_current_state = 1;
/*  676 */           action0();
/*      */         } else {
/*      */           
/*  679 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 37:
/*  685 */         if ($__uri.equals("") && $__local.equals("final")) {
/*  686 */           this.$_ngcc_current_state = 32;
/*      */         } else {
/*      */           
/*  689 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 45:
/*  695 */         if ($__uri.equals("") && $__local.equals("abstract")) {
/*  696 */           this.$_ngcc_current_state = 40;
/*      */         } else {
/*      */           
/*  699 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */ 
/*      */       
/*      */       case 44:
/*  705 */         this.$_ngcc_current_state = 40;
/*  706 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*      */         return;
/*      */ 
/*      */       
/*      */       case 18:
/*  711 */         if ($__uri.equals("") && $__local.equals("nillable")) {
/*  712 */           this.$_ngcc_current_state = 13;
/*      */         } else {
/*      */           
/*  715 */           unexpectedLeaveAttribute($__qname);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/*  721 */     unexpectedLeaveAttribute($__qname);
/*      */   }
/*      */   public void text(String $value) throws SAXException {
/*      */     int $ai;
/*      */     qname qname2;
/*      */     qname qname1;
/*      */     erSet erSet;
/*      */     ersSet ersSet;
/*  729 */     switch (this.$_ngcc_current_state) {
/*      */       
/*      */       case 34:
/*  732 */         this.defaultValue = $value;
/*  733 */         this.$_ngcc_current_state = 33;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 3:
/*  738 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  739 */           this.$runtime.consumeAttribute($ai);
/*  740 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  743 */         this.$_ngcc_current_state = 1;
/*  744 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 23:
/*  750 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  751 */           this.$runtime.consumeAttribute($ai);
/*  752 */           this.$runtime.sendText(this._cookie, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 46:
/*  758 */         this.abstractValue = $value;
/*  759 */         this.$_ngcc_current_state = 45;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 1:
/*  764 */         this.$_ngcc_current_state = 0;
/*  765 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 22:
/*  770 */         this.name = $value;
/*  771 */         this.$_ngcc_current_state = 21;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 15:
/*  776 */         qname2 = new qname(this, this._source, this.$runtime, 333);
/*  777 */         spawnChildFromText((NGCCEventReceiver)qname2, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 11:
/*  782 */         this.$_ngcc_current_state = 3;
/*  783 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 32:
/*  788 */         if (($ai = this.$runtime.getAttributeIndex("", "default")) >= 0) {
/*  789 */           this.$runtime.consumeAttribute($ai);
/*  790 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  793 */         this.$_ngcc_current_state = 28;
/*  794 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 0:
/*  800 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 28:
/*  805 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/*  806 */           this.$runtime.consumeAttribute($ai);
/*  807 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  810 */         this.$_ngcc_current_state = 24;
/*  811 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 19:
/*  817 */         this.nillable = $value;
/*  818 */         this.$_ngcc_current_state = 18;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 24:
/*  823 */         if (($ai = this.$runtime.getAttributeIndex("", "form")) >= 0) {
/*  824 */           this.$runtime.consumeAttribute($ai);
/*  825 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  828 */         this.$_ngcc_current_state = 23;
/*  829 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 17:
/*  835 */         if (($ai = this.$runtime.getAttributeIndex("", "nillable")) >= 0) {
/*  836 */           this.$runtime.consumeAttribute($ai);
/*  837 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  840 */         this.$_ngcc_current_state = 13;
/*  841 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 30:
/*  847 */         this.fixedValue = $value;
/*  848 */         this.$_ngcc_current_state = 29;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 36:
/*  853 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/*  854 */           this.$runtime.consumeAttribute($ai);
/*  855 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  858 */         this.$_ngcc_current_state = 32;
/*  859 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 40:
/*  865 */         if (($ai = this.$runtime.getAttributeIndex("", "block")) >= 0) {
/*  866 */           this.$runtime.consumeAttribute($ai);
/*  867 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  870 */         this.$_ngcc_current_state = 36;
/*  871 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 6:
/*  877 */         qname1 = new qname(this, this._source, this.$runtime, 316);
/*  878 */         spawnChildFromText((NGCCEventReceiver)qname1, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 13:
/*  883 */         if (($ai = this.$runtime.getAttributeIndex("", "substitutionGroup")) >= 0) {
/*  884 */           this.$runtime.consumeAttribute($ai);
/*  885 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  888 */         this.$_ngcc_current_state = 11;
/*  889 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 38:
/*  895 */         erSet = new erSet(this, this._source, this.$runtime, 361);
/*  896 */         spawnChildFromText((NGCCEventReceiver)erSet, $value);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 26:
/*  901 */         if ($value.equals("unqualified")) {
/*  902 */           qualification qualification = new qualification(this, this._source, this.$runtime, 346);
/*  903 */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */           break;
/*      */         } 
/*  906 */         if ($value.equals("qualified")) {
/*  907 */           qualification qualification = new qualification(this, this._source, this.$runtime, 346);
/*  908 */           spawnChildFromText((NGCCEventReceiver)qualification, $value);
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 44:
/*  915 */         if (($ai = this.$runtime.getAttributeIndex("", "abstract")) >= 0) {
/*  916 */           this.$runtime.consumeAttribute($ai);
/*  917 */           this.$runtime.sendText(this._cookie, $value);
/*      */           break;
/*      */         } 
/*  920 */         this.$_ngcc_current_state = 40;
/*  921 */         this.$runtime.sendText(this._cookie, $value);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 42:
/*  927 */         ersSet = new ersSet(this, this._source, this.$runtime, 366);
/*  928 */         spawnChildFromText((NGCCEventReceiver)ersSet, $value);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/*  935 */     switch ($__cookie__) {
/*      */       
/*      */       case 325:
/*  938 */         this.type = (Ref.Type)$__result__;
/*  939 */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 326:
/*  944 */         this.type = (Ref.Type)$__result__;
/*  945 */         this.$_ngcc_current_state = 1;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 313:
/*  950 */         this.$_ngcc_current_state = 0;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 333:
/*  955 */         this.substRef = (UName)$__result__;
/*  956 */         action1();
/*  957 */         this.$_ngcc_current_state = 14;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 312:
/*  962 */         this.$_ngcc_current_state = 0;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 316:
/*  967 */         this.typeName = (UName)$__result__;
/*  968 */         this.$_ngcc_current_state = 5;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 361:
/*  973 */         this.finalValue = (Integer)$__result__;
/*  974 */         this.$_ngcc_current_state = 37;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 366:
/*  979 */         this.blockValue = (Integer)$__result__;
/*  980 */         this.$_ngcc_current_state = 41;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 330:
/*  985 */         this.annotation = (AnnotationImpl)$__result__;
/*  986 */         this.$_ngcc_current_state = 3;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 346:
/*  991 */         this.form = ((Boolean)$__result__).booleanValue();
/*  992 */         action2();
/*  993 */         this.$_ngcc_current_state = 25;
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean accepted() {
/* 1000 */     return (this.$_ngcc_current_state == 13 || this.$_ngcc_current_state == 17 || this.$_ngcc_current_state == 11 || this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ElementDecl makeResult() {
/*      */     String tns;
/* 1011 */     if (this.finalValue == null)
/* 1012 */       this.finalValue = new Integer(this.$runtime.finalDefault); 
/* 1013 */     if (this.blockValue == null) {
/* 1014 */       this.blockValue = new Integer(this.$runtime.blockDefault);
/*      */     }
/* 1016 */     if (!this.formSpecified)
/* 1017 */       this.form = this.$runtime.elementFormDefault; 
/* 1018 */     if (this.isGlobal) {
/* 1019 */       this.form = true;
/*      */     }
/*      */     
/* 1022 */     if (this.form) { tns = this.$runtime.currentSchema.getTargetNamespace(); }
/* 1023 */     else { tns = ""; }
/*      */     
/* 1025 */     if (this.type == null) {
/* 1026 */       if (this.substHeadRef != null) {
/* 1027 */         this.type = (Ref.Type)new SubstGroupBaseTypeRef((Ref.Element)this.substHeadRef);
/*      */       } else {
/* 1029 */         this.type = (Ref.Type)this.$runtime.parser.schemaSet.anyType;
/*      */       } 
/*      */     }
/* 1032 */     ElementDecl ed = new ElementDecl((PatcherManager)this.$runtime, this.$runtime.currentSchema, this.annotation, this.locator, tns, this.name, !this.isGlobal, this.defaultValue, this.fixedValue, this.$runtime.parseBoolean(this.nillable), this.$runtime.parseBoolean(this.abstractValue), this.type, (Ref.Element)this.substHeadRef, this.blockValue.intValue(), this.finalValue.intValue());
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
/* 1050 */     if (this.type instanceof ComplexTypeImpl)
/* 1051 */       ((ComplexTypeImpl)this.type).setScope((XSElementDecl)ed); 
/* 1052 */     return ed;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\elementDeclBody.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
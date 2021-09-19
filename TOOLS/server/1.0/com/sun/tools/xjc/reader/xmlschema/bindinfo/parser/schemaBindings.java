/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BISchemaBinding;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.javadoc;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.nameXmlTransformRule;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ class schemaBindings
/*     */   extends NGCCHandler
/*     */ {
/*     */   private BISchemaBinding.NamingRule at;
/*     */   private String packageName;
/*     */   private BISchemaBinding.NamingRule mt;
/*     */   private String javadoc;
/*     */   private BISchemaBinding.NamingRule nt;
/*     */   private BISchemaBinding.NamingRule tt;
/*     */   private BISchemaBinding.NamingRule et;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  35 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public schemaBindings(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  39 */     super(source, parent, cookie);
/*  40 */     this.$runtime = runtime;
/*  41 */     this.$_ngcc_current_state = 34;
/*     */   }
/*     */   
/*     */   public schemaBindings(NGCCRuntimeEx runtime) {
/*  45 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  49 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  54 */     this.$uri = $__uri;
/*  55 */     this.$localName = $__local;
/*  56 */     this.$qname = $__qname;
/*  57 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/*  60 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/*  61 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 261);
/*  62 */           spawnChildFromEnterElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  65 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 22:
/*  71 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/*  72 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 277);
/*  73 */           spawnChildFromEnterElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  76 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 27:
/*  82 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc") {
/*  83 */           javadoc javadoc = new javadoc(this, this._source, this.$runtime, 305);
/*  84 */           spawnChildFromEnterElement((NGCCEventReceiver)javadoc, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  87 */           this.$_ngcc_current_state = 26;
/*  88 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/*  94 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/*  95 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 273);
/*  96 */           spawnChildFromEnterElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  99 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 34:
/* 105 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") {
/* 106 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 107 */           action0();
/* 108 */           this.$_ngcc_current_state = 25;
/*     */         } else {
/*     */           
/* 111 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 117 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "nameXmlTransform") {
/* 118 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 119 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 122 */           this.$_ngcc_current_state = 1;
/* 123 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 129 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 130 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 269);
/* 131 */           spawnChildFromEnterElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 134 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 140 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName") {
/* 141 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 142 */           this.$_ngcc_current_state = 22;
/*     */         
/*     */         }
/* 145 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName") {
/* 146 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 147 */           this.$_ngcc_current_state = 19;
/*     */         
/*     */         }
/* 150 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName") {
/* 151 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 152 */           this.$_ngcc_current_state = 15;
/*     */         
/*     */         }
/* 155 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName") {
/* 156 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 157 */           this.$_ngcc_current_state = 11;
/*     */         
/*     */         }
/* 160 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName") {
/* 161 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 162 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 165 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 25:
/* 175 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "package") {
/* 176 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 177 */           this.$_ngcc_current_state = 29;
/*     */         } else {
/*     */           
/* 180 */           this.$_ngcc_current_state = 2;
/* 181 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 187 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 192 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 193 */           this.$runtime.consumeAttribute($ai);
/* 194 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 197 */           this.$_ngcc_current_state = 27;
/* 198 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 204 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName") {
/* 205 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 206 */           this.$_ngcc_current_state = 22;
/*     */         
/*     */         }
/* 209 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName") {
/* 210 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 211 */           this.$_ngcc_current_state = 19;
/*     */         
/*     */         }
/* 214 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName") {
/* 215 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 216 */           this.$_ngcc_current_state = 15;
/*     */         
/*     */         }
/* 219 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName") {
/* 220 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 221 */           this.$_ngcc_current_state = 11;
/*     */         
/*     */         }
/* 224 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName") {
/* 225 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 226 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 229 */           this.$_ngcc_current_state = 3;
/* 230 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 240 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 241 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 265);
/* 242 */           spawnChildFromEnterElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 245 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 251 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 259 */     this.$uri = $__uri;
/* 260 */     this.$localName = $__local;
/* 261 */     this.$qname = $__qname;
/* 262 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 265 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName") || (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName") || (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName")) {
/* 266 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 261);
/* 267 */           spawnChildFromLeaveElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 270 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 22:
/* 276 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName") || (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName") || (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName")) {
/* 277 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 277);
/* 278 */           spawnChildFromLeaveElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 281 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 27:
/* 287 */         this.$_ngcc_current_state = 26;
/* 288 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 293 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName") || (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName") || (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName")) {
/* 294 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 273);
/* 295 */           spawnChildFromLeaveElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 298 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 304 */         this.$_ngcc_current_state = 1;
/* 305 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 310 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") {
/* 311 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 312 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 315 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 26:
/* 321 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "package") {
/* 322 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 323 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 326 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 332 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName") || (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName") || (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName")) {
/* 333 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 269);
/* 334 */           spawnChildFromLeaveElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 337 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 343 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "anonymousTypeName") {
/* 344 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 345 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 348 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 354 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "nameXmlTransform") {
/* 355 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 356 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 359 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 365 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName") {
/* 366 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 367 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 370 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 376 */         this.$_ngcc_current_state = 2;
/* 377 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 382 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 387 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 388 */           this.$runtime.consumeAttribute($ai);
/* 389 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 392 */           this.$_ngcc_current_state = 27;
/* 393 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 18:
/* 399 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "elementName") {
/* 400 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 401 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 404 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 21:
/* 410 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typeName") {
/* 411 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 412 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 415 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 421 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "attributeName") {
/* 422 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 423 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 426 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 432 */         this.$_ngcc_current_state = 3;
/* 433 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 438 */         if ((($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName") || (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "modelGroupName")) {
/* 439 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 265);
/* 440 */           spawnChildFromLeaveElement((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 443 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 449 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 456 */     this.$uri = $__uri;
/* 457 */     this.$localName = $__local;
/* 458 */     this.$qname = $__qname;
/* 459 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/* 462 */         if (($__uri == "" && $__local == "suffix") || ($__uri == "" && $__local == "prefix")) {
/* 463 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 269);
/* 464 */           spawnChildFromEnterAttribute((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 467 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 473 */         if (($__uri == "" && $__local == "suffix") || ($__uri == "" && $__local == "prefix")) {
/* 474 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 261);
/* 475 */           spawnChildFromEnterAttribute((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 478 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 22:
/* 484 */         if (($__uri == "" && $__local == "suffix") || ($__uri == "" && $__local == "prefix")) {
/* 485 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 277);
/* 486 */           spawnChildFromEnterAttribute((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 489 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/* 495 */         this.$_ngcc_current_state = 2;
/* 496 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 501 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 19:
/* 506 */         if (($__uri == "" && $__local == "suffix") || ($__uri == "" && $__local == "prefix")) {
/* 507 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 273);
/* 508 */           spawnChildFromEnterAttribute((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 511 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 517 */         if ($__uri == "" && $__local == "name") {
/* 518 */           this.$_ngcc_current_state = 31;
/*     */         } else {
/*     */           
/* 521 */           this.$_ngcc_current_state = 27;
/* 522 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 27:
/* 528 */         this.$_ngcc_current_state = 26;
/* 529 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 534 */         this.$_ngcc_current_state = 1;
/* 535 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 540 */         this.$_ngcc_current_state = 3;
/* 541 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 546 */         if (($__uri == "" && $__local == "suffix") || ($__uri == "" && $__local == "prefix")) {
/* 547 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 265);
/* 548 */           spawnChildFromEnterAttribute((NGCCEventReceiver)nameXmlTransformRule, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 551 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 557 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 564 */     this.$uri = $__uri;
/* 565 */     this.$localName = $__local;
/* 566 */     this.$qname = $__qname;
/* 567 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 25:
/* 570 */         this.$_ngcc_current_state = 2;
/* 571 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 576 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 30:
/* 581 */         if ($__uri == "" && $__local == "name") {
/* 582 */           this.$_ngcc_current_state = 27;
/*     */         } else {
/*     */           
/* 585 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 29:
/* 591 */         this.$_ngcc_current_state = 27;
/* 592 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 27:
/* 597 */         this.$_ngcc_current_state = 26;
/* 598 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 603 */         this.$_ngcc_current_state = 1;
/* 604 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 609 */         this.$_ngcc_current_state = 3;
/* 610 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 615 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 623 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 15:
/* 626 */         if (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 627 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 269);
/* 628 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */           break;
/*     */         } 
/* 631 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0) {
/* 632 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 269);
/* 633 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 640 */         if (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 641 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 261);
/* 642 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */           break;
/*     */         } 
/* 645 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0) {
/* 646 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 261);
/* 647 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 31:
/* 654 */         this.packageName = $value;
/* 655 */         this.$_ngcc_current_state = 30;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 22:
/* 660 */         if (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 661 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 277);
/* 662 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */           break;
/*     */         } 
/* 665 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0) {
/* 666 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 277);
/* 667 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 25:
/* 674 */         this.$_ngcc_current_state = 2;
/* 675 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 680 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 19:
/* 685 */         if (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 686 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 273);
/* 687 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */           break;
/*     */         } 
/* 690 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0) {
/* 691 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 273);
/* 692 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 29:
/* 699 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 700 */           this.$runtime.consumeAttribute($ai);
/* 701 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 704 */         this.$_ngcc_current_state = 27;
/* 705 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 27:
/* 711 */         this.$_ngcc_current_state = 26;
/* 712 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 717 */         this.$_ngcc_current_state = 1;
/* 718 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 723 */         this.$_ngcc_current_state = 3;
/* 724 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 729 */         if (($ai = this.$runtime.getAttributeIndex("", "prefix")) >= 0) {
/* 730 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 265);
/* 731 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */           break;
/*     */         } 
/* 734 */         if (($ai = this.$runtime.getAttributeIndex("", "suffix")) >= 0) {
/* 735 */           nameXmlTransformRule nameXmlTransformRule = new nameXmlTransformRule(this, this._source, this.$runtime, 265);
/* 736 */           spawnChildFromText((NGCCEventReceiver)nameXmlTransformRule, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 745 */     switch ($__cookie__) {
/*     */       
/*     */       case 305:
/* 748 */         this.javadoc = (String)$__result__;
/* 749 */         this.$_ngcc_current_state = 26;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 261:
/* 754 */         this.nt = (BISchemaBinding.NamingRule)$__result__;
/* 755 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 277:
/* 760 */         this.tt = (BISchemaBinding.NamingRule)$__result__;
/* 761 */         this.$_ngcc_current_state = 21;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 273:
/* 766 */         this.et = (BISchemaBinding.NamingRule)$__result__;
/* 767 */         this.$_ngcc_current_state = 18;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 269:
/* 772 */         this.at = (BISchemaBinding.NamingRule)$__result__;
/* 773 */         this.$_ngcc_current_state = 14;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 265:
/* 778 */         this.mt = (BISchemaBinding.NamingRule)$__result__;
/* 779 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 786 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BISchemaBinding makeResult() {
/* 792 */     return new BISchemaBinding(this.packageName, this.javadoc, this.tt, this.et, this.at, this.mt, this.nt, this.loc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\schemaBindings.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
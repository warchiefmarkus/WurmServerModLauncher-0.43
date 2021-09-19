/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.ModelGroupImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.modelGroupBody;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class group
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private ModelGroupImpl term;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $localName; protected String $qname; private ModelGroupDeclImpl result; private Locator loc; private Locator mloc; private String compositorName;
/*     */   public group(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 15;
/*     */   }
/*     */   
/*     */   public group(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  44 */     this.result = new ModelGroupDeclImpl(this.$runtime.currentSchema, this.annotation, this.loc, this.$runtime.currentSchema.getTargetNamespace(), this.name, this.term);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  55 */     this.mloc = this.$runtime.copyLocator();
/*  56 */     this.compositorName = this.$localName;
/*     */   }
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  61 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  66 */     this.$uri = $__uri;
/*  67 */     this.$localName = $__local;
/*  68 */     this.$qname = $__qname;
/*  69 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/*  72 */         action1();
/*  73 */         this.$_ngcc_current_state = 4;
/*  74 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  79 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  80 */           this.$runtime.consumeAttribute($ai);
/*  81 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  84 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/*  90 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/*  91 */           this.$runtime.consumeAttribute($ai);
/*  92 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  95 */           this.$_ngcc_current_state = 10;
/*  96 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 102 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 107 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 108 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 109 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 112 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 15:
/* 118 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 119 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 120 */           action2();
/* 121 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 124 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 130 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 131 */           annotation annotation = new annotation(this, this._source, this.$runtime, 404, null, AnnotationContext.MODELGROUP_DECL);
/* 132 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 135 */           this.$_ngcc_current_state = 5;
/* 136 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 142 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("element")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("any")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation"))) {
/* 143 */           modelGroupBody modelGroupBody = new modelGroupBody(this, this._source, this.$runtime, 400, this.mloc, this.compositorName);
/* 144 */           spawnChildFromEnterElement((NGCCEventReceiver)modelGroupBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 147 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 153 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 161 */     this.$uri = $__uri;
/* 162 */     this.$localName = $__local;
/* 163 */     this.$qname = $__qname;
/* 164 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 167 */         action1();
/* 168 */         this.$_ngcc_current_state = 4;
/* 169 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 174 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 175 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 176 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 179 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 185 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 186 */           this.$runtime.consumeAttribute($ai);
/* 187 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 190 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 196 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("group")) {
/* 197 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 198 */           this.$_ngcc_current_state = 0;
/* 199 */           action0();
/*     */         } else {
/*     */           
/* 202 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 208 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 209 */           this.$runtime.consumeAttribute($ai);
/* 210 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 213 */           this.$_ngcc_current_state = 10;
/* 214 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 220 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 225 */         this.$_ngcc_current_state = 5;
/* 226 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 231 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("all")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("choice")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("sequence"))) {
/* 232 */           modelGroupBody modelGroupBody = new modelGroupBody(this, this._source, this.$runtime, 400, this.mloc, this.compositorName);
/* 233 */           spawnChildFromLeaveElement((NGCCEventReceiver)modelGroupBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 236 */           unexpectedLeaveElement($__qname);
/*     */         } 
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
/*     */       case 5:
/* 256 */         action1();
/* 257 */         this.$_ngcc_current_state = 4;
/* 258 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 263 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 264 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 267 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 273 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 274 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 277 */           this.$_ngcc_current_state = 10;
/* 278 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 284 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 289 */         this.$_ngcc_current_state = 5;
/* 290 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 295 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 303 */     this.$uri = $__uri;
/* 304 */     this.$localName = $__local;
/* 305 */     this.$qname = $__qname;
/* 306 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 309 */         if ($__uri.equals("") && $__local.equals("ID")) {
/* 310 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 313 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 319 */         action1();
/* 320 */         this.$_ngcc_current_state = 4;
/* 321 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 326 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 327 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 330 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 336 */         this.$_ngcc_current_state = 10;
/* 337 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 342 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 347 */         this.$_ngcc_current_state = 5;
/* 348 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 353 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 361 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 364 */         action1();
/* 365 */         this.$_ngcc_current_state = 4;
/* 366 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 371 */         this.name = $value;
/* 372 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 377 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 378 */           this.$runtime.consumeAttribute($ai);
/* 379 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 385 */         this.$_ngcc_current_state = 12;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 390 */         if (($ai = this.$runtime.getAttributeIndex("", "ID")) >= 0) {
/* 391 */           this.$runtime.consumeAttribute($ai);
/* 392 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 395 */         this.$_ngcc_current_state = 10;
/* 396 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 402 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 407 */         this.$_ngcc_current_state = 5;
/* 408 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 415 */     switch ($__cookie__) {
/*     */       
/*     */       case 404:
/* 418 */         this.annotation = (AnnotationImpl)$__result__;
/* 419 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 400:
/* 424 */         this.term = (ModelGroupImpl)$__result__;
/* 425 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 432 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\group.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
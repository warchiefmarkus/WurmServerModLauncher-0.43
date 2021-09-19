/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.SimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.SimpleType_List;
/*     */ import com.sun.xml.xsom.impl.parser.state.SimpleType_Restriction;
/*     */ import com.sun.xml.xsom.impl.parser.state.SimpleType_Union;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class simpleType
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String __text;
/*     */   private String name;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $uri; protected String $localName; protected String $qname; private SimpleTypeImpl result; private Locator locator;
/*     */   public simpleType(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 25;
/*     */   }
/*     */   
/*     */   public simpleType(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     this.$runtime.processList(this.__text);
/*     */   }
/*     */   private void action1() throws SAXException {
/*  46 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  51 */     this.$uri = $__uri;
/*  52 */     this.$localName = $__local;
/*  53 */     this.$qname = $__qname;
/*  54 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/*  57 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  58 */           this.$runtime.consumeAttribute($ai);
/*  59 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  62 */           this.$_ngcc_current_state = 7;
/*  63 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/*  69 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  70 */           annotation annotation = new annotation(this, this._source, this.$runtime, 280, null, AnnotationContext.SIMPLETYPE_DECL);
/*  71 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  74 */           this.$_ngcc_current_state = 2;
/*  75 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 25:
/*  81 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/*  82 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  83 */           action1();
/*  84 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/*  87 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  93 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/*  94 */           SimpleType_Restriction simpleType_Restriction = new SimpleType_Restriction(this, this._source, this.$runtime, 277, this.annotation, this.locator, this.name);
/*  95 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType_Restriction, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  98 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/*  99 */           SimpleType_List simpleType_List = new SimpleType_List(this, this._source, this.$runtime, 278, this.annotation, this.locator, this.name);
/* 100 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType_List, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 103 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("union")) {
/* 104 */           SimpleType_Union simpleType_Union = new SimpleType_Union(this, this._source, this.$runtime, 272, this.annotation, this.locator, this.name);
/* 105 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType_Union, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 108 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 13:
/* 116 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 117 */           this.$runtime.consumeAttribute($ai);
/* 118 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 121 */           this.$_ngcc_current_state = 9;
/* 122 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 128 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 133 */         this.$_ngcc_current_state = 14;
/* 134 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 147 */     this.$uri = $__uri;
/* 148 */     this.$localName = $__local;
/* 149 */     this.$qname = $__qname;
/* 150 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 153 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 154 */           this.$runtime.consumeAttribute($ai);
/* 155 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 158 */           this.$_ngcc_current_state = 7;
/* 159 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 165 */         this.$_ngcc_current_state = 2;
/* 166 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 171 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 172 */           this.$runtime.consumeAttribute($ai);
/* 173 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 176 */           this.$_ngcc_current_state = 9;
/* 177 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 183 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 184 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 185 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 188 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 194 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 199 */         this.$_ngcc_current_state = 14;
/* 200 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 205 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 213 */     this.$uri = $__uri;
/* 214 */     this.$localName = $__local;
/* 215 */     this.$qname = $__qname;
/* 216 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 219 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 220 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 223 */           this.$_ngcc_current_state = 7;
/* 224 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 230 */         this.$_ngcc_current_state = 2;
/* 231 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 236 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 237 */           this.$_ngcc_current_state = 15;
/*     */         } else {
/*     */           
/* 240 */           this.$_ngcc_current_state = 9;
/* 241 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 247 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 252 */         this.$_ngcc_current_state = 14;
/* 253 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 266 */     this.$uri = $__uri;
/* 267 */     this.$localName = $__local;
/* 268 */     this.$qname = $__qname;
/* 269 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 272 */         this.$_ngcc_current_state = 7;
/* 273 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 278 */         this.$_ngcc_current_state = 2;
/* 279 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 284 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 285 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 288 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 294 */         this.$_ngcc_current_state = 9;
/* 295 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 300 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 305 */         if ($__uri.equals("") && $__local.equals("final")) {
/* 306 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 309 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 16:
/* 315 */         this.$_ngcc_current_state = 14;
/* 316 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 321 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 329 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 9:
/* 332 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 333 */           this.$runtime.consumeAttribute($ai);
/* 334 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 337 */         this.$_ngcc_current_state = 7;
/* 338 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 344 */         this.$_ngcc_current_state = 2;
/* 345 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 15:
/* 350 */         if ($value.equals("#all")) {
/* 351 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 354 */         this.__text = $value;
/* 355 */         this.$_ngcc_current_state = 16;
/* 356 */         action0();
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 13:
/* 362 */         if (($ai = this.$runtime.getAttributeIndex("", "final")) >= 0) {
/* 363 */           this.$runtime.consumeAttribute($ai);
/* 364 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 367 */         this.$_ngcc_current_state = 9;
/* 368 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 374 */         this.name = $value;
/* 375 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 380 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 14:
/* 385 */         if ($value.equals("list")) {
/* 386 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 389 */         if ($value.equals("union")) {
/* 390 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 393 */         if ($value.equals("restriction")) {
/* 394 */           this.$_ngcc_current_state = 14;
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/* 402 */         if ($value.equals("list")) {
/* 403 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 406 */         if ($value.equals("union")) {
/* 407 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 410 */         if ($value.equals("restriction")) {
/* 411 */           this.$_ngcc_current_state = 14;
/*     */           break;
/*     */         } 
/* 414 */         this.$_ngcc_current_state = 14;
/* 415 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 425 */     switch ($__cookie__) {
/*     */       
/*     */       case 280:
/* 428 */         this.annotation = (AnnotationImpl)$__result__;
/* 429 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 277:
/* 434 */         this.result = (SimpleTypeImpl)$__result__;
/* 435 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 278:
/* 440 */         this.result = (SimpleTypeImpl)$__result__;
/* 441 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 272:
/* 446 */         this.result = (SimpleTypeImpl)$__result__;
/* 447 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 454 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\simpleType.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
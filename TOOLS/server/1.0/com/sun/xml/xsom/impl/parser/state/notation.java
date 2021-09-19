/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.NotationImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class notation extends NGCCHandler {
/*     */   private String name;
/*     */   private String pub;
/*     */   private String sys;
/*     */   private AnnotationImpl ann;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public notation(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.$_ngcc_current_state = 14;
/*     */   }
/*     */   
/*     */   public notation(NGCCRuntimeEx runtime) {
/*  40 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  44 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  49 */     this.$uri = $__uri;
/*  50 */     this.$localName = $__local;
/*  51 */     this.$qname = $__qname;
/*  52 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/*  55 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/*  56 */           this.$runtime.consumeAttribute($ai);
/*  57 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  60 */           this.$_ngcc_current_state = 2;
/*  61 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  67 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/*  68 */           this.$runtime.consumeAttribute($ai);
/*  69 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  72 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  78 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  79 */           annotation annotation = new annotation(this, this._source, this.$runtime, 255, null, AnnotationContext.NOTATION);
/*  80 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  83 */           this.$_ngcc_current_state = 1;
/*  84 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/*  90 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  91 */           this.$runtime.consumeAttribute($ai);
/*  92 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  95 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 14:
/* 101 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 102 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 103 */           action0();
/* 104 */           this.$_ngcc_current_state = 13;
/*     */         } else {
/*     */           
/* 107 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 113 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 126 */     this.$uri = $__uri;
/* 127 */     this.$localName = $__local;
/* 128 */     this.$qname = $__qname;
/* 129 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 132 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 133 */           this.$runtime.consumeAttribute($ai);
/* 134 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 137 */           this.$_ngcc_current_state = 2;
/* 138 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 144 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("notation")) {
/* 145 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 146 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 149 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 155 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 156 */           this.$runtime.consumeAttribute($ai);
/* 157 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 160 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 166 */         this.$_ngcc_current_state = 1;
/* 167 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 172 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 173 */           this.$runtime.consumeAttribute($ai);
/* 174 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 177 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 183 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 188 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 196 */     this.$uri = $__uri;
/* 197 */     this.$localName = $__local;
/* 198 */     this.$qname = $__qname;
/* 199 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 202 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 203 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 206 */           this.$_ngcc_current_state = 2;
/* 207 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 213 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 214 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 217 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 223 */         this.$_ngcc_current_state = 1;
/* 224 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/* 229 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 230 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/* 233 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 239 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 244 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 252 */     this.$uri = $__uri;
/* 253 */     this.$localName = $__local;
/* 254 */     this.$qname = $__qname;
/* 255 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 258 */         this.$_ngcc_current_state = 2;
/* 259 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 264 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 265 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 268 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 274 */         if ($__uri.equals("") && $__local.equals("system")) {
/* 275 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 278 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 284 */         if ($__uri.equals("") && $__local.equals("public")) {
/* 285 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 288 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 294 */         this.$_ngcc_current_state = 1;
/* 295 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 300 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 305 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 313 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 316 */         this.name = $value;
/* 317 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 322 */         if (($ai = this.$runtime.getAttributeIndex("", "system")) >= 0) {
/* 323 */           this.$runtime.consumeAttribute($ai);
/* 324 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 327 */         this.$_ngcc_current_state = 2;
/* 328 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 6:
/* 334 */         this.sys = $value;
/* 335 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 340 */         this.pub = $value;
/* 341 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 346 */         if (($ai = this.$runtime.getAttributeIndex("", "public")) >= 0) {
/* 347 */           this.$runtime.consumeAttribute($ai);
/* 348 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 354 */         this.$_ngcc_current_state = 1;
/* 355 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 13:
/* 360 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 361 */           this.$runtime.consumeAttribute($ai);
/* 362 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 368 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 375 */     switch ($__cookie__) {
/*     */       
/*     */       case 255:
/* 378 */         this.ann = (AnnotationImpl)$__result__;
/* 379 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 386 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private XSNotation makeResult() {
/* 392 */     String tns = this.$runtime.currentSchema.getTargetNamespace();
/* 393 */     return (XSNotation)new NotationImpl(this.$runtime.currentSchema, this.ann, this.loc, tns, this.name, this.pub, this.sys);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\notation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
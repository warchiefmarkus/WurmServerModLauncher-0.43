/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.AttGroupDeclImpl;
/*     */ import com.sun.xml.xsom.impl.AttributesHolder;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.attributeUses;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ class attributeGroupDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $uri; protected String $localName; protected String $qname; private AttGroupDeclImpl result; private Locator locator;
/*     */   public attributeGroupDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 13;
/*     */   }
/*     */   
/*     */   public attributeGroupDecl(NGCCRuntimeEx runtime) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     this.result = new AttGroupDeclImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  49 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  54 */     this.$uri = $__uri;
/*  55 */     this.$localName = $__local;
/*  56 */     this.$qname = $__qname;
/*  57 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  60 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 13:
/*  65 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/*  66 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  67 */           action1();
/*  68 */           this.$_ngcc_current_state = 12;
/*     */         } else {
/*     */           
/*  71 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  77 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/*  78 */           this.$runtime.consumeAttribute($ai);
/*  79 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  82 */           this.$_ngcc_current_state = 4;
/*  83 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/*  89 */         action0();
/*  90 */         this.$_ngcc_current_state = 2;
/*  91 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  96 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  97 */           annotation annotation = new annotation(this, this._source, this.$runtime, 495, null, AnnotationContext.ATTRIBUTE_GROUP);
/*  98 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 101 */           this.$_ngcc_current_state = 3;
/* 102 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 108 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attribute")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("anyAttribute"))) {
/* 109 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 492, (AttributesHolder)this.result);
/* 110 */           spawnChildFromEnterElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 113 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 119 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 120 */           this.$runtime.consumeAttribute($ai);
/* 121 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 124 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 130 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 138 */     this.$uri = $__uri;
/* 139 */     this.$localName = $__local;
/* 140 */     this.$qname = $__qname;
/* 141 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 144 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 149 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 150 */           this.$runtime.consumeAttribute($ai);
/* 151 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 154 */           this.$_ngcc_current_state = 4;
/* 155 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 161 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 162 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 163 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 166 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 172 */         action0();
/* 173 */         this.$_ngcc_current_state = 2;
/* 174 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 179 */         this.$_ngcc_current_state = 3;
/* 180 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 185 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("attributeGroup")) {
/* 186 */           attributeUses attributeUses = new attributeUses(this, this._source, this.$runtime, 492, (AttributesHolder)this.result);
/* 187 */           spawnChildFromLeaveElement((NGCCEventReceiver)attributeUses, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 190 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 196 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 197 */           this.$runtime.consumeAttribute($ai);
/* 198 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 201 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 207 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 215 */     this.$uri = $__uri;
/* 216 */     this.$localName = $__local;
/* 217 */     this.$qname = $__qname;
/* 218 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 221 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 226 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 227 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 230 */           this.$_ngcc_current_state = 4;
/* 231 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 237 */         action0();
/* 238 */         this.$_ngcc_current_state = 2;
/* 239 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 244 */         this.$_ngcc_current_state = 3;
/* 245 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 250 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 251 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 254 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 260 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 268 */     this.$uri = $__uri;
/* 269 */     this.$localName = $__local;
/* 270 */     this.$qname = $__qname;
/* 271 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 274 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 279 */         if ($__uri.equals("") && $__local.equals("id")) {
/* 280 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 283 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 289 */         this.$_ngcc_current_state = 4;
/* 290 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 295 */         if ($__uri.equals("") && $__local.equals("name")) {
/* 296 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 299 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 305 */         action0();
/* 306 */         this.$_ngcc_current_state = 2;
/* 307 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 312 */         this.$_ngcc_current_state = 3;
/* 313 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 318 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 326 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 329 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 334 */         if (($ai = this.$runtime.getAttributeIndex("", "id")) >= 0) {
/* 335 */           this.$runtime.consumeAttribute($ai);
/* 336 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 339 */         this.$_ngcc_current_state = 4;
/* 340 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 346 */         this.name = $value;
/* 347 */         this.$_ngcc_current_state = 10;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 352 */         action0();
/* 353 */         this.$_ngcc_current_state = 2;
/* 354 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 359 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 364 */         this.$_ngcc_current_state = 3;
/* 365 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 12:
/* 370 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 371 */           this.$runtime.consumeAttribute($ai);
/* 372 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 380 */     switch ($__cookie__) {
/*     */       
/*     */       case 492:
/* 383 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 495:
/* 388 */         this.annotation = (AnnotationImpl)$__result__;
/* 389 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 396 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\attributeGroupDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
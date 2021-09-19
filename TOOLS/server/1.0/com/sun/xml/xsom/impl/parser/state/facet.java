/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.FacetImpl;
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
/*     */ class facet extends NGCCHandler {
/*     */   private AnnotationImpl annotation;
/*     */   private String fixed;
/*     */   private String value;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private FacetImpl result;
/*     */   private Locator locator;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public facet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 11;
/*     */   }
/*     */   
/*     */   public facet(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  44 */     this.result = new FacetImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.$localName, this.value, this.$runtime.createValidationContext(), this.$runtime.parseBoolean(this.fixed));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  51 */     this.locator = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  56 */     this.$uri = $__uri;
/*  57 */     this.$localName = $__local;
/*  58 */     this.$qname = $__qname;
/*  59 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 11:
/*  62 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  63 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  64 */           action1();
/*  65 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/*  68 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  74 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/*  75 */           this.$runtime.consumeAttribute($ai);
/*  76 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  79 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  85 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  86 */           annotation annotation = new annotation(this, this._source, this.$runtime, 671, null, AnnotationContext.SIMPLETYPE_DECL);
/*  87 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  90 */           this.$_ngcc_current_state = 1;
/*  91 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  97 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 102 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 103 */           this.$runtime.consumeAttribute($ai);
/* 104 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 107 */           this.$_ngcc_current_state = 2;
/* 108 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 114 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 122 */     this.$uri = $__uri;
/* 123 */     this.$localName = $__local;
/* 124 */     this.$qname = $__qname;
/* 125 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 128 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 129 */           this.$runtime.consumeAttribute($ai);
/* 130 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 133 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 139 */         this.$_ngcc_current_state = 1;
/* 140 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 145 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 150 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 151 */           this.$runtime.consumeAttribute($ai);
/* 152 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 155 */           this.$_ngcc_current_state = 2;
/* 156 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 162 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 163 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 164 */           this.$_ngcc_current_state = 0;
/* 165 */           action0();
/*     */         } else {
/*     */           
/* 168 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 174 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 182 */     this.$uri = $__uri;
/* 183 */     this.$localName = $__local;
/* 184 */     this.$qname = $__qname;
/* 185 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 188 */         if ($__uri.equals("") && $__local.equals("value")) {
/* 189 */           this.$_ngcc_current_state = 9;
/*     */         } else {
/*     */           
/* 192 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 198 */         this.$_ngcc_current_state = 1;
/* 199 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 204 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 209 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 210 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 213 */           this.$_ngcc_current_state = 2;
/* 214 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 220 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 228 */     this.$uri = $__uri;
/* 229 */     this.$localName = $__local;
/* 230 */     this.$qname = $__qname;
/* 231 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 234 */         if ($__uri.equals("") && $__local.equals("fixed")) {
/* 235 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 238 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 244 */         this.$_ngcc_current_state = 1;
/* 245 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 250 */         if ($__uri.equals("") && $__local.equals("value")) {
/* 251 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 254 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 260 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 265 */         this.$_ngcc_current_state = 2;
/* 266 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 271 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 279 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 282 */         if (($ai = this.$runtime.getAttributeIndex("", "value")) >= 0) {
/* 283 */           this.$runtime.consumeAttribute($ai);
/* 284 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 290 */         this.$_ngcc_current_state = 1;
/* 291 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 9:
/* 296 */         this.value = $value;
/* 297 */         this.$_ngcc_current_state = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 302 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 307 */         this.fixed = $value;
/* 308 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 313 */         if (($ai = this.$runtime.getAttributeIndex("", "fixed")) >= 0) {
/* 314 */           this.$runtime.consumeAttribute($ai);
/* 315 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 318 */         this.$_ngcc_current_state = 2;
/* 319 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 327 */     switch ($__cookie__) {
/*     */       
/*     */       case 671:
/* 330 */         this.annotation = (AnnotationImpl)$__result__;
/* 331 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 338 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\facet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
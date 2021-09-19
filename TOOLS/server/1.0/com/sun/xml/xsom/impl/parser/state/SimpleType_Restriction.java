/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.facet;
/*     */ import com.sun.xml.xsom.impl.parser.state.qname;
/*     */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class SimpleType_Restriction extends NGCCHandler {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName baseTypeName;
/*     */   private XSFacet facet;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   private int $_ngcc_current_state; protected String $uri; protected String $localName; protected String $qname; private RestrictionSimpleTypeImpl result; private Ref.SimpleType baseType; private Locator rloc;
/*     */   public SimpleType_Restriction(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, String _name) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.annotation = _annotation;
/*  38 */     this.locator = _locator;
/*  39 */     this.name = _name;
/*  40 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public SimpleType_Restriction(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, String _name) {
/*  44 */     this(null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _name);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  49 */     this.result.addFacet(this.facet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  55 */     this.result = new RestrictionSimpleTypeImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.name, (this.name == null), this.baseType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  62 */     this.baseType = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.rloc, this.$runtime.currentSchema, this.baseTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action3() throws SAXException {
/*  68 */     this.rloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  73 */     this.$uri = $__uri;
/*  74 */     this.$localName = $__local;
/*  75 */     this.$qname = $__qname;
/*  76 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/*  79 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  80 */           annotation annotation = new annotation(this, this._source, this.$runtime, 637, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/*  81 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  84 */           this.$_ngcc_current_state = 5;
/*  85 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  91 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/*  92 */           facet facet = new facet(this, this._source, this.$runtime, 627);
/*  93 */           spawnChildFromEnterElement((NGCCEventReceiver)facet, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  96 */           this.$_ngcc_current_state = 1;
/*  97 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 103 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 104 */           this.$runtime.consumeAttribute($ai);
/* 105 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 108 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 109 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 631);
/* 110 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 113 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 120 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxExclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxInclusive")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("totalDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("fractionDigits")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("length")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("maxLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("minLength")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("enumeration")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("whiteSpace")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("pattern"))) {
/* 121 */           facet facet = new facet(this, this._source, this.$runtime, 626);
/* 122 */           spawnChildFromEnterElement((NGCCEventReceiver)facet, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 125 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/* 131 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 132 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 133 */           action3();
/* 134 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 137 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 143 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 148 */         action1();
/* 149 */         this.$_ngcc_current_state = 2;
/* 150 */         this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 155 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 163 */     this.$uri = $__uri;
/* 164 */     this.$localName = $__local;
/* 165 */     this.$qname = $__qname;
/* 166 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 169 */         this.$_ngcc_current_state = 5;
/* 170 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 175 */         this.$_ngcc_current_state = 1;
/* 176 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 181 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 182 */           this.$runtime.consumeAttribute($ai);
/* 183 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 186 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 192 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("restriction")) {
/* 193 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 194 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 197 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 203 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 208 */         action1();
/* 209 */         this.$_ngcc_current_state = 2;
/* 210 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 215 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 223 */     this.$uri = $__uri;
/* 224 */     this.$localName = $__local;
/* 225 */     this.$qname = $__qname;
/* 226 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 229 */         this.$_ngcc_current_state = 5;
/* 230 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 235 */         this.$_ngcc_current_state = 1;
/* 236 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 241 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 242 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 245 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 251 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 256 */         action1();
/* 257 */         this.$_ngcc_current_state = 2;
/* 258 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 263 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 271 */     this.$uri = $__uri;
/* 272 */     this.$localName = $__local;
/* 273 */     this.$qname = $__qname;
/* 274 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 277 */         if ($__uri.equals("") && $__local.equals("base")) {
/* 278 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 281 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 287 */         this.$_ngcc_current_state = 5;
/* 288 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 293 */         this.$_ngcc_current_state = 1;
/* 294 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 299 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 304 */         action1();
/* 305 */         this.$_ngcc_current_state = 2;
/* 306 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 311 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     qname qname;
/* 319 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 322 */         this.$_ngcc_current_state = 5;
/* 323 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 328 */         this.$_ngcc_current_state = 1;
/* 329 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 334 */         if (($ai = this.$runtime.getAttributeIndex("", "base")) >= 0) {
/* 335 */           this.$runtime.consumeAttribute($ai);
/* 336 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 342 */         qname = new qname(this, this._source, this.$runtime, 633);
/* 343 */         spawnChildFromText((NGCCEventReceiver)qname, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 348 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 353 */         action1();
/* 354 */         this.$_ngcc_current_state = 2;
/* 355 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 362 */     switch ($__cookie__) {
/*     */       
/*     */       case 637:
/* 365 */         this.annotation = (AnnotationImpl)$__result__;
/* 366 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 631:
/* 371 */         this.baseType = (Ref.SimpleType)$__result__;
/* 372 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 626:
/* 377 */         this.facet = (XSFacet)$__result__;
/* 378 */         action0();
/* 379 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 627:
/* 384 */         this.facet = (XSFacet)$__result__;
/* 385 */         action0();
/* 386 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 633:
/* 391 */         this.baseTypeName = (UName)$__result__;
/* 392 */         action2();
/* 393 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 400 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\SimpleType_Restriction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.WildcardImpl;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import java.util.HashSet;
/*     */ import java.util.StringTokenizer;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class wildcardBody
/*     */   extends NGCCHandler
/*     */ {
/*     */   private AnnotationImpl annotation;
/*     */   private Locator locator;
/*     */   private String modeValue;
/*     */   private String ns;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  32 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public wildcardBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, Locator _locator) {
/*  36 */     super(source, parent, cookie);
/*  37 */     this.$runtime = runtime;
/*  38 */     this.locator = _locator;
/*  39 */     this.$_ngcc_current_state = 9;
/*     */   }
/*     */   
/*     */   public wildcardBody(NGCCRuntimeEx runtime, Locator _locator) {
/*  43 */     this(null, (NGCCEventSource)runtime, runtime, -1, _locator);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  48 */     this.$uri = $__uri;
/*  49 */     this.$localName = $__local;
/*  50 */     this.$qname = $__qname;
/*  51 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  54 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  55 */           this.$runtime.consumeAttribute($ai);
/*  56 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  59 */           this.$_ngcc_current_state = 0;
/*  60 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  66 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/*  67 */           this.$runtime.consumeAttribute($ai);
/*  68 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  71 */           this.$_ngcc_current_state = 1;
/*  72 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/*  78 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  79 */           annotation annotation = new annotation(this, this._source, this.$runtime, 565, null, AnnotationContext.WILDCARD);
/*  80 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  83 */           this.$_ngcc_current_state = 5;
/*  84 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  90 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  95 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 103 */     this.$uri = $__uri;
/* 104 */     this.$localName = $__local;
/* 105 */     this.$qname = $__qname;
/* 106 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 109 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 110 */           this.$runtime.consumeAttribute($ai);
/* 111 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 114 */           this.$_ngcc_current_state = 0;
/* 115 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 121 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 122 */           this.$runtime.consumeAttribute($ai);
/* 123 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 126 */           this.$_ngcc_current_state = 1;
/* 127 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 133 */         this.$_ngcc_current_state = 5;
/* 134 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 139 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 144 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 152 */     this.$uri = $__uri;
/* 153 */     this.$localName = $__local;
/* 154 */     this.$qname = $__qname;
/* 155 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 158 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 159 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 162 */           this.$_ngcc_current_state = 0;
/* 163 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 169 */         if ($__uri.equals("") && $__local.equals("processContents")) {
/* 170 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/* 173 */           this.$_ngcc_current_state = 1;
/* 174 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 180 */         this.$_ngcc_current_state = 5;
/* 181 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 186 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 199 */     this.$uri = $__uri;
/* 200 */     this.$localName = $__local;
/* 201 */     this.$qname = $__qname;
/* 202 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 205 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 206 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 209 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 215 */         this.$_ngcc_current_state = 0;
/* 216 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 221 */         this.$_ngcc_current_state = 1;
/* 222 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 227 */         this.$_ngcc_current_state = 5;
/* 228 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 233 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 238 */         if ($__uri.equals("") && $__local.equals("processContents")) {
/* 239 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 242 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 256 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 259 */         this.ns = $value;
/* 260 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 7:
/* 265 */         this.modeValue = $value;
/* 266 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 271 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 272 */           this.$runtime.consumeAttribute($ai);
/* 273 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 276 */         this.$_ngcc_current_state = 0;
/* 277 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 5:
/* 283 */         if (($ai = this.$runtime.getAttributeIndex("", "processContents")) >= 0) {
/* 284 */           this.$runtime.consumeAttribute($ai);
/* 285 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 288 */         this.$_ngcc_current_state = 1;
/* 289 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 9:
/* 295 */         this.$_ngcc_current_state = 5;
/* 296 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 301 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 308 */     switch ($__cookie__) {
/*     */       
/*     */       case 565:
/* 311 */         this.annotation = (AnnotationImpl)$__result__;
/* 312 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 319 */     return (this.$_ngcc_current_state == 0 || this.$_ngcc_current_state == 9 || this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 5);
/*     */   }
/*     */ 
/*     */   
/*     */   private WildcardImpl makeResult() {
/* 324 */     if (this.modeValue == null) this.modeValue = "strict";
/*     */     
/* 326 */     int mode = -1;
/* 327 */     if (this.modeValue.equals("strict")) mode = 2; 
/* 328 */     if (this.modeValue.equals("lax")) mode = 1; 
/* 329 */     if (this.modeValue.equals("skip")) mode = 3; 
/* 330 */     if (mode == -1) throw new InternalError();
/*     */     
/* 332 */     if (this.ns == null || this.ns.equals("##any")) {
/* 333 */       return (WildcardImpl)new WildcardImpl.Any(this.$runtime.currentSchema, this.annotation, this.locator, mode);
/*     */     }
/* 335 */     if (this.ns.equals("##other")) {
/* 336 */       return (WildcardImpl)new WildcardImpl.Other(this.$runtime.currentSchema, this.annotation, this.locator, this.$runtime.currentSchema.getTargetNamespace(), mode);
/*     */     }
/*     */ 
/*     */     
/* 340 */     StringTokenizer tokens = new StringTokenizer(this.ns);
/* 341 */     HashSet s = new HashSet();
/* 342 */     while (tokens.hasMoreTokens()) {
/* 343 */       String ns = tokens.nextToken();
/* 344 */       if (ns.equals("##local")) ns = ""; 
/* 345 */       if (ns.equals("##targetNamespace")) ns = this.$runtime.currentSchema.getTargetNamespace(); 
/* 346 */       s.add(ns);
/*     */     } 
/*     */     
/* 349 */     return (WildcardImpl)new WildcardImpl.Finite(this.$runtime.currentSchema, this.annotation, this.locator, s, mode);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\wildcardBody.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
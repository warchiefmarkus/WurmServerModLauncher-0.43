/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class identityConstraint
/*     */   extends NGCCHandler
/*     */ {
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  26 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public identityConstraint(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  30 */     super(source, parent, cookie);
/*  31 */     this.$runtime = runtime;
/*  32 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public identityConstraint(NGCCRuntimeEx runtime) {
/*  36 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  41 */     this.$uri = $__uri;
/*  42 */     this.$localName = $__local;
/*  43 */     this.$qname = $__qname;
/*  44 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/*  47 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/*  48 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  49 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/*  52 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/*  58 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")) {
/*  59 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  60 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/*  63 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/*  69 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  70 */           annotation annotation = new annotation(this, this._source, this.$runtime, 584, null, AnnotationContext.IDENTITY_CONSTRAINT);
/*  71 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  74 */           this.$_ngcc_current_state = 6;
/*  75 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  81 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/*  82 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  83 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/*  86 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  92 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  93 */           annotation annotation = new annotation(this, this._source, this.$runtime, 588, null, AnnotationContext.IDENTITY_CONSTRAINT);
/*  94 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  97 */           this.$_ngcc_current_state = 9;
/*  98 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 104 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/* 105 */           annotation annotation = new annotation(this, this._source, this.$runtime, 578, null, AnnotationContext.IDENTITY_CONSTRAINT);
/* 106 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 109 */           this.$_ngcc_current_state = 2;
/* 110 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 116 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 121 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/* 122 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 123 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 126 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 132 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/* 140 */     this.$uri = $__uri;
/* 141 */     this.$localName = $__local;
/* 142 */     this.$qname = $__qname;
/* 143 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 146 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("selector")) {
/* 147 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 148 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 151 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 157 */         this.$_ngcc_current_state = 6;
/* 158 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 163 */         if (($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("key")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("keyref")) || ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("unique"))) {
/* 164 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 165 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 168 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 174 */         this.$_ngcc_current_state = 9;
/* 175 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 180 */         this.$_ngcc_current_state = 2;
/* 181 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 186 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 191 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("field")) {
/* 192 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 193 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 196 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 202 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 210 */     this.$uri = $__uri;
/* 211 */     this.$localName = $__local;
/* 212 */     this.$qname = $__qname;
/* 213 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 216 */         this.$_ngcc_current_state = 6;
/* 217 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 222 */         this.$_ngcc_current_state = 9;
/* 223 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 228 */         this.$_ngcc_current_state = 2;
/* 229 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 234 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 239 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 247 */     this.$uri = $__uri;
/* 248 */     this.$localName = $__local;
/* 249 */     this.$qname = $__qname;
/* 250 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 253 */         this.$_ngcc_current_state = 6;
/* 254 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 259 */         this.$_ngcc_current_state = 9;
/* 260 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 265 */         this.$_ngcc_current_state = 2;
/* 266 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 271 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 276 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 284 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 287 */         this.$_ngcc_current_state = 6;
/* 288 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 293 */         this.$_ngcc_current_state = 9;
/* 294 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 299 */         this.$_ngcc_current_state = 2;
/* 300 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 305 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 312 */     switch ($__cookie__) {
/*     */       
/*     */       case 584:
/* 315 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 578:
/* 320 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 588:
/* 325 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 332 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\identityConstraint.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
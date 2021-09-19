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
/*     */ class importDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String ns;
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public importDecl(NGCCRuntimeEx runtime) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     if (this.ns == null) this.ns = ""; 
/*  44 */     this.$runtime.importSchema(this.ns, this.schemaLocation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  50 */     this.$uri = $__uri;
/*  51 */     this.$localName = $__local;
/*  52 */     this.$qname = $__qname;
/*  53 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/*  56 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  57 */           annotation annotation = new annotation(this, this._source, this.$runtime, 377, null, AnnotationContext.SCHEMA);
/*  58 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  61 */           this.$_ngcc_current_state = 1;
/*  62 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/*  68 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  69 */           this.$runtime.consumeAttribute($ai);
/*  70 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  73 */           this.$_ngcc_current_state = 4;
/*  74 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  80 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  81 */           this.$runtime.consumeAttribute($ai);
/*  82 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  85 */           this.$_ngcc_current_state = 2;
/*  86 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  92 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 12:
/*  97 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/*  98 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  99 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 102 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 108 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 116 */     this.$uri = $__uri;
/* 117 */     this.$localName = $__local;
/* 118 */     this.$qname = $__qname;
/* 119 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 122 */         this.$_ngcc_current_state = 1;
/* 123 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 128 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 129 */           this.$runtime.consumeAttribute($ai);
/* 130 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 133 */           this.$_ngcc_current_state = 4;
/* 134 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 140 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("import")) {
/* 141 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 142 */           this.$_ngcc_current_state = 0;
/* 143 */           action0();
/*     */         } else {
/*     */           
/* 146 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 152 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 153 */           this.$runtime.consumeAttribute($ai);
/* 154 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 157 */           this.$_ngcc_current_state = 2;
/* 158 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 164 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 169 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 177 */     this.$uri = $__uri;
/* 178 */     this.$localName = $__local;
/* 179 */     this.$qname = $__qname;
/* 180 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 183 */         this.$_ngcc_current_state = 1;
/* 184 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 189 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 190 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/* 193 */           this.$_ngcc_current_state = 4;
/* 194 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 200 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 201 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/* 204 */           this.$_ngcc_current_state = 2;
/* 205 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 211 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 216 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 224 */     this.$uri = $__uri;
/* 225 */     this.$localName = $__local;
/* 226 */     this.$qname = $__qname;
/* 227 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 230 */         this.$_ngcc_current_state = 1;
/* 231 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 8:
/* 236 */         this.$_ngcc_current_state = 4;
/* 237 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/* 242 */         if ($__uri.equals("") && $__local.equals("namespace")) {
/* 243 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 246 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 252 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 253 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 256 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 262 */         this.$_ngcc_current_state = 2;
/* 263 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 268 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 273 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 281 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 284 */         this.$_ngcc_current_state = 1;
/* 285 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 290 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 291 */           this.$runtime.consumeAttribute($ai);
/* 292 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 295 */         this.$_ngcc_current_state = 4;
/* 296 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 6:
/* 302 */         this.schemaLocation = $value;
/* 303 */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 308 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 309 */           this.$runtime.consumeAttribute($ai);
/* 310 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 313 */         this.$_ngcc_current_state = 2;
/* 314 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 320 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 325 */         this.ns = $value;
/* 326 */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 333 */     switch ($__cookie__) {
/*     */       
/*     */       case 377:
/* 336 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 343 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\importDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
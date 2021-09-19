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
/*     */ class includeDecl
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String schemaLocation;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  27 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public includeDecl(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  31 */     super(source, parent, cookie);
/*  32 */     this.$runtime = runtime;
/*  33 */     this.$_ngcc_current_state = 7;
/*     */   }
/*     */   
/*     */   public includeDecl(NGCCRuntimeEx runtime) {
/*  37 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  41 */     this.$runtime.includeSchema(this.schemaLocation);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  46 */     this.$uri = $__uri;
/*  47 */     this.$localName = $__local;
/*  48 */     this.$qname = $__qname;
/*  49 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/*  52 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/*  53 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  54 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/*  57 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  63 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/*  64 */           this.$runtime.consumeAttribute($ai);
/*  65 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  68 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  74 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  79 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  80 */           annotation annotation = new annotation(this, this._source, this.$runtime, 569, null, AnnotationContext.SCHEMA);
/*  81 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  84 */           this.$_ngcc_current_state = 1;
/*  85 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  91 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  99 */     this.$uri = $__uri;
/* 100 */     this.$localName = $__local;
/* 101 */     this.$qname = $__qname;
/* 102 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 105 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 106 */           this.$runtime.consumeAttribute($ai);
/* 107 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 110 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 116 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 121 */         this.$_ngcc_current_state = 1;
/* 122 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 127 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("include")) {
/* 128 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 129 */           this.$_ngcc_current_state = 0;
/* 130 */           action0();
/*     */         } else {
/*     */           
/* 133 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 147 */     this.$uri = $__uri;
/* 148 */     this.$localName = $__local;
/* 149 */     this.$qname = $__qname;
/* 150 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 153 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 154 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 157 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 163 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 168 */         this.$_ngcc_current_state = 1;
/* 169 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 174 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 182 */     this.$uri = $__uri;
/* 183 */     this.$localName = $__local;
/* 184 */     this.$qname = $__qname;
/* 185 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 188 */         if ($__uri.equals("") && $__local.equals("schemaLocation")) {
/* 189 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 192 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 198 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 203 */         this.$_ngcc_current_state = 1;
/* 204 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 209 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 217 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 5:
/* 220 */         this.schemaLocation = $value;
/* 221 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 226 */         if (($ai = this.$runtime.getAttributeIndex("", "schemaLocation")) >= 0) {
/* 227 */           this.$runtime.consumeAttribute($ai);
/* 228 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 234 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 239 */         this.$_ngcc_current_state = 1;
/* 240 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 247 */     switch ($__cookie__) {
/*     */       
/*     */       case 569:
/* 250 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 257 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\includeDecl.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
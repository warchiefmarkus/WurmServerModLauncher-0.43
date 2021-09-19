/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ import com.sun.xml.xsom.impl.AnnotationImpl;
/*     */ import com.sun.xml.xsom.impl.ListSimpleTypeImpl;
/*     */ import com.sun.xml.xsom.impl.Ref;
/*     */ import com.sun.xml.xsom.impl.UName;
/*     */ import com.sun.xml.xsom.impl.parser.DelayedRef;
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.PatcherManager;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import com.sun.xml.xsom.impl.parser.state.annotation;
/*     */ import com.sun.xml.xsom.impl.parser.state.qname;
/*     */ import com.sun.xml.xsom.impl.parser.state.simpleType;
/*     */ import com.sun.xml.xsom.parser.AnnotationContext;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class SimpleType_List extends NGCCHandler {
/*     */   private Locator locator;
/*     */   private AnnotationImpl annotation;
/*     */   private String name;
/*     */   private UName itemTypeName;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $uri; protected String $localName; protected String $qname; private ListSimpleTypeImpl result; private Ref.SimpleType itemType; private Locator lloc;
/*     */   public SimpleType_List(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, AnnotationImpl _annotation, Locator _locator, String _name) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.annotation = _annotation;
/*  37 */     this.locator = _locator;
/*  38 */     this.name = _name;
/*  39 */     this.$_ngcc_current_state = 9;
/*     */   }
/*     */   
/*     */   public SimpleType_List(NGCCRuntimeEx runtime, AnnotationImpl _annotation, Locator _locator, String _name) {
/*  43 */     this(null, (NGCCEventSource)runtime, runtime, -1, _annotation, _locator, _name);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action0() throws SAXException {
/*  48 */     this.result = new ListSimpleTypeImpl(this.$runtime.currentSchema, this.annotation, this.locator, this.name, (this.name == null), this.itemType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void action1() throws SAXException {
/*  56 */     this.itemType = (Ref.SimpleType)new DelayedRef.SimpleType((PatcherManager)this.$runtime, this.lloc, this.$runtime.currentSchema, this.itemTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void action2() throws SAXException {
/*  62 */     this.lloc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  67 */     this.$uri = $__uri;
/*  68 */     this.$localName = $__local;
/*  69 */     this.$qname = $__qname;
/*  70 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/*  73 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("annotation")) {
/*  74 */           annotation annotation = new annotation(this, this._source, this.$runtime, 648, this.annotation, AnnotationContext.SIMPLETYPE_DECL);
/*  75 */           spawnChildFromEnterElement((NGCCEventReceiver)annotation, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  78 */           this.$_ngcc_current_state = 2;
/*  79 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 9:
/*  85 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/*  86 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  87 */           action2();
/*  88 */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           
/*  91 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  97 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/*  98 */           this.$runtime.consumeAttribute($ai);
/*  99 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 102 */         else if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("simpleType")) {
/* 103 */           simpleType simpleType = new simpleType(this, this._source, this.$runtime, 642);
/* 104 */           spawnChildFromEnterElement((NGCCEventReceiver)simpleType, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/* 107 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 114 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 119 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 127 */     this.$uri = $__uri;
/* 128 */     this.$localName = $__local;
/* 129 */     this.$qname = $__qname;
/* 130 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 133 */         this.$_ngcc_current_state = 2;
/* 134 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 139 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 140 */           this.$runtime.consumeAttribute($ai);
/* 141 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 144 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 150 */         if ($__uri.equals("http://www.w3.org/2001/XMLSchema") && $__local.equals("list")) {
/* 151 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 152 */           this.$_ngcc_current_state = 0;
/* 153 */           action0();
/*     */         } else {
/*     */           
/* 156 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 162 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 167 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 175 */     this.$uri = $__uri;
/* 176 */     this.$localName = $__local;
/* 177 */     this.$qname = $__qname;
/* 178 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 181 */         this.$_ngcc_current_state = 2;
/* 182 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 187 */         if ($__uri.equals("") && $__local.equals("itemType")) {
/* 188 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 191 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 197 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 202 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 210 */     this.$uri = $__uri;
/* 211 */     this.$localName = $__local;
/* 212 */     this.$qname = $__qname;
/* 213 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 216 */         this.$_ngcc_current_state = 2;
/* 217 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 222 */         if ($__uri.equals("") && $__local.equals("itemType")) {
/* 223 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 226 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 232 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 237 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     qname qname;
/* 245 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 7:
/* 248 */         this.$_ngcc_current_state = 2;
/* 249 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 254 */         if (($ai = this.$runtime.getAttributeIndex("", "itemType")) >= 0) {
/* 255 */           this.$runtime.consumeAttribute($ai);
/* 256 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 262 */         qname = new qname(this, this._source, this.$runtime, 644);
/* 263 */         spawnChildFromText((NGCCEventReceiver)qname, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 268 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 275 */     switch ($__cookie__) {
/*     */       
/*     */       case 648:
/* 278 */         this.annotation = (AnnotationImpl)$__result__;
/* 279 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 642:
/* 284 */         this.itemType = (Ref.SimpleType)$__result__;
/* 285 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 644:
/* 290 */         this.itemTypeName = (UName)$__result__;
/* 291 */         action1();
/* 292 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 299 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\SimpleType_List.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
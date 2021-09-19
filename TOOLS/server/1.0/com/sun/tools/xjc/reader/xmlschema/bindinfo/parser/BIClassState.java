/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIClass;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.javadoc;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BIClassState
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String implClass;
/*     */   private String javadoc;
/*     */   private String name;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public BIClassState(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*  36 */     this.$runtime = runtime;
/*  37 */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public BIClassState(NGCCRuntimeEx runtime) {
/*  41 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  45 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  50 */     this.$uri = $__uri;
/*  51 */     this.$localName = $__local;
/*  52 */     this.$qname = $__qname;
/*  53 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/*  56 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") {
/*  57 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  58 */           action0();
/*  59 */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           
/*  62 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/*  68 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc") {
/*  69 */           javadoc javadoc = new javadoc(this, this._source, this.$runtime, 414);
/*  70 */           spawnChildFromEnterElement((NGCCEventReceiver)javadoc, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  73 */           this.$_ngcc_current_state = 6;
/*  74 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  80 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
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
/*     */       case 2:
/*  92 */         if (($ai = this.$runtime.getAttributeIndex("", "implClass")) >= 0) {
/*  93 */           this.$runtime.consumeAttribute($ai);
/*  94 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  97 */           this.$_ngcc_current_state = 1;
/*  98 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 104 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 109 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/* 117 */     this.$uri = $__uri;
/* 118 */     this.$localName = $__local;
/* 119 */     this.$qname = $__qname;
/* 120 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 123 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") {
/* 124 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 125 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 128 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 134 */         this.$_ngcc_current_state = 6;
/* 135 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 140 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 141 */           this.$runtime.consumeAttribute($ai);
/* 142 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 145 */           this.$_ngcc_current_state = 2;
/* 146 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 152 */         if (($ai = this.$runtime.getAttributeIndex("", "implClass")) >= 0) {
/* 153 */           this.$runtime.consumeAttribute($ai);
/* 154 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 157 */           this.$_ngcc_current_state = 1;
/* 158 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 164 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
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
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 176 */     this.$uri = $__uri;
/* 177 */     this.$localName = $__local;
/* 178 */     this.$qname = $__qname;
/* 179 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/* 182 */         this.$_ngcc_current_state = 6;
/* 183 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 188 */         if ($__uri == "" && $__local == "name") {
/* 189 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 192 */           this.$_ngcc_current_state = 2;
/* 193 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 199 */         if ($__uri == "" && $__local == "implClass") {
/* 200 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 203 */           this.$_ngcc_current_state = 1;
/* 204 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 210 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 215 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 222 */     this.$uri = $__uri;
/* 223 */     this.$localName = $__local;
/* 224 */     this.$qname = $__qname;
/* 225 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 228 */         if ($__uri == "" && $__local == "implClass") {
/* 229 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 232 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 10:
/* 238 */         this.$_ngcc_current_state = 6;
/* 239 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 244 */         this.$_ngcc_current_state = 2;
/* 245 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 250 */         this.$_ngcc_current_state = 1;
/* 251 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 256 */         if ($__uri == "" && $__local == "name") {
/* 257 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 260 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 266 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
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
/*     */       case 4:
/* 282 */         this.implClass = $value;
/* 283 */         this.$_ngcc_current_state = 3;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 10:
/* 288 */         this.$_ngcc_current_state = 6;
/* 289 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 294 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 295 */           this.$runtime.consumeAttribute($ai);
/* 296 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 299 */         this.$_ngcc_current_state = 2;
/* 300 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 306 */         if (($ai = this.$runtime.getAttributeIndex("", "implClass")) >= 0) {
/* 307 */           this.$runtime.consumeAttribute($ai);
/* 308 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 311 */         this.$_ngcc_current_state = 1;
/* 312 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 318 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 323 */         this.name = $value;
/* 324 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 331 */     switch ($__cookie__) {
/*     */       
/*     */       case 414:
/* 334 */         this.javadoc = (String)$__result__;
/* 335 */         this.$_ngcc_current_state = 6;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 342 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BIClass makeResult() {
/* 348 */     return new BIClass(this.loc, this.name, this.implClass, this.javadoc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\BIClassState.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
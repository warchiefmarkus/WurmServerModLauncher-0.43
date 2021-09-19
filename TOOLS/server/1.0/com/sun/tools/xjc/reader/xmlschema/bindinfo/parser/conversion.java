/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.conversionBody;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class conversion
/*     */   extends NGCCHandler
/*     */ {
/*     */   private BIConversion r;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public conversion(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 3;
/*     */   }
/*     */   
/*     */   public conversion(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  44 */     this.$uri = $__uri;
/*  45 */     this.$localName = $__local;
/*  46 */     this.$qname = $__qname;
/*  47 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  50 */         revertToParentFromEnterElement(this.r, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/*  55 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType") {
/*  56 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  57 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/*  60 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  66 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0 || ($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0) {
/*  67 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/*  68 */           spawnChildFromEnterElement((NGCCEventReceiver)conversionBody, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  71 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  77 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  85 */     this.$uri = $__uri;
/*  86 */     this.$localName = $__local;
/*  87 */     this.$qname = $__qname;
/*  88 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  91 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType") {
/*  92 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  93 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/*  96 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 102 */         revertToParentFromLeaveElement(this.r, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 107 */         if ((($ai = this.$runtime.getAttributeIndex("", "name")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType") || (($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType") || (($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0 && $__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType")) {
/* 108 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/* 109 */           spawnChildFromLeaveElement((NGCCEventReceiver)conversionBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 112 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 125 */     this.$uri = $__uri;
/* 126 */     this.$localName = $__local;
/* 127 */     this.$qname = $__qname;
/* 128 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 131 */         revertToParentFromEnterAttribute(this.r, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 136 */         if (($__uri == "" && $__local == "name") || ($__uri == "" && $__local == "parseMethod") || ($__uri == "" && $__local == "printMethod")) {
/* 137 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/* 138 */           spawnChildFromEnterAttribute((NGCCEventReceiver)conversionBody, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 141 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 147 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 154 */     this.$uri = $__uri;
/* 155 */     this.$localName = $__local;
/* 156 */     this.$qname = $__qname;
/* 157 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 160 */         revertToParentFromLeaveAttribute(this.r, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 165 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 173 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 176 */         revertToParentFromText(this.r, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 181 */         if (($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0) {
/* 182 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/* 183 */           spawnChildFromText((NGCCEventReceiver)conversionBody, $value);
/*     */           break;
/*     */         } 
/* 186 */         if (($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0) {
/* 187 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/* 188 */           spawnChildFromText((NGCCEventReceiver)conversionBody, $value);
/*     */           break;
/*     */         } 
/* 191 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 192 */           conversionBody conversionBody = new conversionBody(this, this._source, this.$runtime, 421);
/* 193 */           spawnChildFromText((NGCCEventReceiver)conversionBody, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 203 */     switch ($__cookie__) {
/*     */       
/*     */       case 421:
/* 206 */         this.r = (BIConversion)$__result__;
/* 207 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 214 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\conversion.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.tools.xjc.grammar.xducer.FacadeTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.IdentityTransducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.Transducer;
/*     */ import com.sun.tools.xjc.grammar.xducer.UserTransducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.MagicTransducer;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ class conversionBody extends NGCCHandler {
/*     */   private String _context;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private String type;
/*     */   private String parse;
/*     */   private String print;
/*     */   private boolean context;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  31 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public conversionBody(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  35 */     super(source, parent, cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 442 */     this.type = "java.lang.String";
/* 443 */     this.parse = null;
/* 444 */     this.print = null;
/* 445 */     this.context = false;
/*     */     this.$runtime = runtime;
/*     */     this.$_ngcc_current_state = 12;
/*     */   }
/*     */   
/*     */   public conversionBody(NGCCRuntimeEx runtime) {
/*     */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*     */     this.context = this.$runtime.parseBoolean(this._context);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 0:
/*     */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */       case 7:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 7;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "hasNsContext")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterElement($__qname);
/*     */   }
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 0:
/*     */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 7:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 7;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "hasNsContext")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 0:
/*     */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 7:
/*     */         if ($__uri == "" && $__local == "name") {
/*     */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         if ($__uri == "" && $__local == "printMethod") {
/*     */           this.$_ngcc_current_state = 10;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 7;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 1:
/*     */         if ($__uri == "" && $__local == "hasNsContext") {
/*     */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 0;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */       case 12:
/*     */         if ($__uri == "" && $__local == "parseMethod") {
/*     */           this.$_ngcc_current_state = 14;
/*     */         } else {
/*     */           this.$_ngcc_current_state = 8;
/*     */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     this.$uri = $__uri;
/*     */     this.$localName = $__local;
/*     */     this.$qname = $__qname;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 13:
/*     */         if ($__uri == "" && $__local == "parseMethod") {
/*     */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 0:
/*     */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 2:
/*     */         if ($__uri == "" && $__local == "hasNsContext") {
/*     */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 5:
/*     */         if ($__uri == "" && $__local == "name") {
/*     */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 9:
/*     */         if ($__uri == "" && $__local == "printMethod") {
/*     */           this.$_ngcc_current_state = 7;
/*     */         } else {
/*     */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */       case 8:
/*     */         this.$_ngcc_current_state = 7;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 1:
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */       case 12:
/*     */         this.$_ngcc_current_state = 8;
/*     */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/*     */     switch (this.$_ngcc_current_state) {
/*     */       case 10:
/*     */         this.print = $value;
/*     */         this.$_ngcc_current_state = 9;
/*     */         break;
/*     */       case 14:
/*     */         this.parse = $value;
/*     */         this.$_ngcc_current_state = 13;
/*     */         break;
/*     */       case 6:
/*     */         this.type = $value;
/*     */         this.$_ngcc_current_state = 5;
/*     */         break;
/*     */       case 0:
/*     */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */       case 7:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */       case 8:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "printMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 7;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 3:
/*     */         this._context = $value;
/*     */         this.$_ngcc_current_state = 2;
/*     */         action0();
/*     */         break;
/*     */       case 1:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "hasNsContext")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 0;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */       case 12:
/*     */         if (($ai = this.$runtime.getAttributeIndex("", "parseMethod")) >= 0) {
/*     */           this.$runtime.consumeAttribute($ai);
/*     */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/*     */         this.$_ngcc_current_state = 8;
/*     */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */   
/*     */   public boolean accepted() {
/*     */     return (this.$_ngcc_current_state == 1 || this.$_ngcc_current_state == 0);
/*     */   }
/*     */   
/*     */   public BIConversion makeResult() throws SAXException {
/*     */     IdentityTransducer identityTransducer;
/*     */     Transducer xducer = null;
/*     */     MagicTransducer magic = null;
/*     */     try {
/*     */       MagicTransducer magicTransducer;
/*     */       FacadeTransducer facadeTransducer;
/*     */       JType typeObj = this.$runtime.getType(this.type);
/*     */       if (this.print == null || this.parse == null)
/*     */         magic = new MagicTransducer(typeObj); 
/*     */       if (this.print != null || this.parse != null)
/*     */         UserTransducer userTransducer = new UserTransducer(typeObj, (this.parse != null) ? this.parse : "new", (this.print != null) ? this.print : "toString", this.context); 
/*     */       if (this.print == null && this.parse == null)
/*     */         magicTransducer = magic; 
/*     */       if (this.print == null && this.parse != null)
/*     */         facadeTransducer = new FacadeTransducer((Transducer)magic, (Transducer)magicTransducer); 
/*     */       if (this.print != null && this.parse == null)
/*     */         facadeTransducer = new FacadeTransducer((Transducer)facadeTransducer, (Transducer)magic); 
/*     */     } catch (IllegalArgumentException e) {
/*     */       this.$runtime.errorHandler.error(new SAXParseException(e.getMessage(), this.$runtime.getLocator()));
/*     */       identityTransducer = new IdentityTransducer(this.$runtime.codeModel);
/*     */     } 
/*     */     BIConversion r = new BIConversion(this.$runtime.copyLocator(), (Transducer)identityTransducer);
/*     */     if (magic != null)
/*     */       magic.setParent(r); 
/*     */     return r;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\conversionBody.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
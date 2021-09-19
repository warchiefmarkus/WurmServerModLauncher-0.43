/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIEnumMember;
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
/*     */ class enumMember
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   private String javadoc;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  30 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public enumMember(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  34 */     super(source, parent, cookie);
/*  35 */     this.$runtime = runtime;
/*  36 */     this.$_ngcc_current_state = 7;
/*     */   }
/*     */   
/*     */   public enumMember(NGCCRuntimeEx runtime) {
/*  40 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  44 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  49 */     this.$uri = $__uri;
/*  50 */     this.$localName = $__local;
/*  51 */     this.$qname = $__qname;
/*  52 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  55 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/*  60 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") {
/*  61 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  62 */           action0();
/*  63 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/*  66 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  72 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc") {
/*  73 */           javadoc javadoc = new javadoc(this, this._source, this.$runtime, 96);
/*  74 */           spawnChildFromEnterElement((NGCCEventReceiver)javadoc, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  77 */           this.$_ngcc_current_state = 1;
/*  78 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  84 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  85 */           this.$runtime.consumeAttribute($ai);
/*  86 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  89 */           unexpectedEnterElement($__qname);
/*     */         } 
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
/*     */       case 0:
/* 109 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 114 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") {
/* 115 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 116 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 119 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 125 */         this.$_ngcc_current_state = 1;
/* 126 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 131 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 132 */           this.$runtime.consumeAttribute($ai);
/* 133 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 136 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 142 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 149 */     this.$uri = $__uri;
/* 150 */     this.$localName = $__local;
/* 151 */     this.$qname = $__qname;
/* 152 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 155 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 160 */         this.$_ngcc_current_state = 1;
/* 161 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/* 166 */         if ($__uri == "" && $__local == "name") {
/* 167 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 170 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 176 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 183 */     this.$uri = $__uri;
/* 184 */     this.$localName = $__local;
/* 185 */     this.$qname = $__qname;
/* 186 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 189 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 194 */         this.$_ngcc_current_state = 1;
/* 195 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 200 */         if ($__uri == "" && $__local == "name") {
/* 201 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 204 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 210 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 218 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 221 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 226 */         this.$_ngcc_current_state = 1;
/* 227 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 232 */         this.name = $value;
/* 233 */         this.$_ngcc_current_state = 4;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 6:
/* 238 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 239 */           this.$runtime.consumeAttribute($ai);
/* 240 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 248 */     switch ($__cookie__) {
/*     */       
/*     */       case 96:
/* 251 */         this.javadoc = (String)$__result__;
/* 252 */         this.$_ngcc_current_state = 1;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 259 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BIEnumMember makeResult() {
/* 265 */     return new BIEnumMember(this.loc, this.name, this.javadoc);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\enumMember.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
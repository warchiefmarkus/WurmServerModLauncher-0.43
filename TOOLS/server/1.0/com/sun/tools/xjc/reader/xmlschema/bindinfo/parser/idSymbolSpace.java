/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIXIdSymbolSpace;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class idSymbolSpace
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String name;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private Locator loc;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  29 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public idSymbolSpace(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */   
/*     */   public idSymbolSpace(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     this.loc = this.$runtime.copyLocator();
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  48 */     this.$uri = $__uri;
/*  49 */     this.$localName = $__local;
/*  50 */     this.$qname = $__qname;
/*  51 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  54 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/*  59 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/*  60 */           this.$runtime.consumeAttribute($ai);
/*  61 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  64 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  70 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") {
/*  71 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  72 */           action0();
/*  73 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/*  76 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  82 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  90 */     this.$uri = $__uri;
/*  91 */     this.$localName = $__local;
/*  92 */     this.$qname = $__qname;
/*  93 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  96 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") {
/*  97 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  98 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 101 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 107 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 112 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 113 */           this.$runtime.consumeAttribute($ai);
/* 114 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 117 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 123 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 130 */     this.$uri = $__uri;
/* 131 */     this.$localName = $__local;
/* 132 */     this.$qname = $__qname;
/* 133 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 136 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 141 */         if ($__uri == "" && $__local == "name") {
/* 142 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 145 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 151 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 158 */     this.$uri = $__uri;
/* 159 */     this.$localName = $__local;
/* 160 */     this.$qname = $__qname;
/* 161 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 164 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 169 */         if ($__uri == "" && $__local == "name") {
/* 170 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 173 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 179 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 187 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 190 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 195 */         this.name = $value;
/* 196 */         this.$_ngcc_current_state = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 201 */         if (($ai = this.$runtime.getAttributeIndex("", "name")) >= 0) {
/* 202 */           this.$runtime.consumeAttribute($ai);
/* 203 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 216 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private BIXIdSymbolSpace makeResult() {
/* 222 */     return new BIXIdSymbolSpace(this.loc, this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\idSymbolSpace.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
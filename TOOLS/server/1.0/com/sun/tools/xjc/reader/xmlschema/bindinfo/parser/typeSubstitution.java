/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class typeSubstitution
/*     */   extends NGCCHandler
/*     */ {
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
/*     */   public typeSubstitution(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 5;
/*     */   }
/*     */   
/*     */   public typeSubstitution(NGCCRuntimeEx runtime) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  43 */     this.$uri = $__uri;
/*  44 */     this.$localName = $__local;
/*  45 */     this.$qname = $__qname;
/*  46 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/*  49 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/*  50 */           this.$runtime.consumeAttribute($ai);
/*  51 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  54 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  60 */         revertToParentFromEnterElement(new Boolean(true), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/*  65 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "typeSubstitution") {
/*  66 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  67 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/*  70 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  76 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  84 */     this.$uri = $__uri;
/*  85 */     this.$localName = $__local;
/*  86 */     this.$qname = $__qname;
/*  87 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  90 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "typeSubstitution") {
/*  91 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  92 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/*  95 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 101 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 102 */           this.$runtime.consumeAttribute($ai);
/* 103 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 106 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 112 */         revertToParentFromLeaveElement(new Boolean(true), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 124 */     this.$uri = $__uri;
/* 125 */     this.$localName = $__local;
/* 126 */     this.$qname = $__qname;
/* 127 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 4:
/* 130 */         if ($__uri == "" && $__local == "type") {
/* 131 */           this.$_ngcc_current_state = 3;
/*     */         } else {
/*     */           
/* 134 */           unexpectedEnterAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 140 */         revertToParentFromEnterAttribute(new Boolean(true), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 145 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 152 */     this.$uri = $__uri;
/* 153 */     this.$localName = $__local;
/* 154 */     this.$qname = $__qname;
/* 155 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 158 */         revertToParentFromLeaveAttribute(new Boolean(true), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 163 */         if ($__uri == "" && $__local == "type") {
/* 164 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 167 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 173 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 181 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/* 184 */         if ($value.equals("complex")) {
/* 185 */           this.$_ngcc_current_state = 2;
/*     */         }
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 191 */         if (($ai = this.$runtime.getAttributeIndex("", "type")) >= 0) {
/* 192 */           this.$runtime.consumeAttribute($ai);
/* 193 */           this.$runtime.sendText(this._cookie, $value);
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 199 */         revertToParentFromText(new Boolean(true), this._cookie, $value);
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
/* 211 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\typeSubstitution.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
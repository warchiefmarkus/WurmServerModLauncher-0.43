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
/*     */ class javadoc
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String javadoc;
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
/*     */   public javadoc(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  33 */     super(source, parent, cookie);
/*  34 */     this.$runtime = runtime;
/*  35 */     this.$_ngcc_current_state = 3;
/*     */   }
/*     */   
/*     */   public javadoc(NGCCRuntimeEx runtime) {
/*  39 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  43 */     this.javadoc = this.$runtime.truncateDocComment(this.javadoc);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  47 */     this.$uri = $__uri;
/*  48 */     this.$localName = $__local;
/*  49 */     this.$qname = $__qname;
/*  50 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 3:
/*  53 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc") {
/*  54 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  55 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/*  58 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  64 */         revertToParentFromEnterElement(this.javadoc, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  69 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  76 */     this.$uri = $__uri;
/*  77 */     this.$localName = $__local;
/*  78 */     this.$qname = $__qname;
/*  79 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  82 */         revertToParentFromLeaveElement(this.javadoc, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/*  87 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javadoc") {
/*  88 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/*  89 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/*  92 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  98 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 105 */     this.$uri = $__uri;
/* 106 */     this.$localName = $__local;
/* 107 */     this.$qname = $__qname;
/* 108 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 111 */         revertToParentFromEnterAttribute(this.javadoc, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 116 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 123 */     this.$uri = $__uri;
/* 124 */     this.$localName = $__local;
/* 125 */     this.$qname = $__qname;
/* 126 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 129 */         revertToParentFromLeaveAttribute(this.javadoc, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 141 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 2:
/* 144 */         this.javadoc = $value;
/* 145 */         this.$_ngcc_current_state = 1;
/* 146 */         action0();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 151 */         revertToParentFromText(this.javadoc, this._cookie, $value);
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
/* 163 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\javadoc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
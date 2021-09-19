/*     */ package 1.0.com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCEventSource;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCHandler;
/*     */ import com.sun.xml.xsom.impl.parser.state.NGCCRuntime;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class erSet
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String v;
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
/*     */   public erSet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  31 */     super(source, parent, cookie);
/*  32 */     this.$runtime = runtime;
/*  33 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public erSet(NGCCRuntimeEx runtime) {
/*  37 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  42 */     this.$uri = $__uri;
/*  43 */     this.$localName = $__local;
/*  44 */     this.$qname = $__qname;
/*  45 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  48 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  53 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  61 */     this.$uri = $__uri;
/*  62 */     this.$localName = $__local;
/*  63 */     this.$qname = $__qname;
/*  64 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  67 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  72 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  80 */     this.$uri = $__uri;
/*  81 */     this.$localName = $__local;
/*  82 */     this.$qname = $__qname;
/*  83 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  86 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  91 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  99 */     this.$uri = $__uri;
/* 100 */     this.$localName = $__local;
/* 101 */     this.$qname = $__qname;
/* 102 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 105 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 110 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 118 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/* 121 */         this.v = $value;
/* 122 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 127 */         revertToParentFromText(makeResult(), this._cookie, $value);
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
/* 139 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private Integer makeResult() {
/* 144 */     if (this.v == null) return new Integer(this.$runtime.finalDefault);
/*     */     
/* 146 */     if (this.v.indexOf("#all") != -1) {
/* 147 */       return new Integer(3);
/*     */     }
/* 149 */     int r = 0;
/*     */     
/* 151 */     if (this.v.indexOf("extension") != -1) r |= 0x1; 
/* 152 */     if (this.v.indexOf("restriction") != -1) r |= 0x2;
/*     */     
/* 154 */     return new Integer(r);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\erSet.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
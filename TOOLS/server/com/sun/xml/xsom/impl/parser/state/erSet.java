/*     */ package com.sun.xml.xsom.impl.parser.state;
/*     */ 
/*     */ import com.sun.xml.xsom.impl.parser.NGCCRuntimeEx;
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
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public erSet(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public erSet(NGCCRuntimeEx runtime) {
/*  38 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  43 */     this.$uri = $__uri;
/*  44 */     this.$localName = $__local;
/*  45 */     this.$qname = $__qname;
/*  46 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  49 */         revertToParentFromEnterElement(makeResult(), this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  54 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*  62 */     this.$uri = $__uri;
/*  63 */     this.$localName = $__local;
/*  64 */     this.$qname = $__qname;
/*  65 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  68 */         revertToParentFromLeaveElement(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  73 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/*  81 */     this.$uri = $__uri;
/*  82 */     this.$localName = $__local;
/*  83 */     this.$qname = $__qname;
/*  84 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/*  87 */         revertToParentFromEnterAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  92 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 100 */     this.$uri = $__uri;
/* 101 */     this.$localName = $__local;
/* 102 */     this.$qname = $__qname;
/* 103 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 106 */         revertToParentFromLeaveAttribute(makeResult(), this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 111 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 119 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 122 */         revertToParentFromText(makeResult(), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 127 */         this.v = $value;
/* 128 */         this.$_ngcc_current_state = 0;
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
/* 140 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private Integer makeResult() {
/* 145 */     if (this.v == null) return new Integer(this.$runtime.finalDefault);
/*     */     
/* 147 */     if (this.v.indexOf("#all") != -1) {
/* 148 */       return new Integer(3);
/*     */     }
/* 150 */     int r = 0;
/*     */     
/* 152 */     if (this.v.indexOf("extension") != -1) r |= 0x1; 
/* 153 */     if (this.v.indexOf("restriction") != -1) r |= 0x2;
/*     */     
/* 155 */     return new Integer(r);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\erSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
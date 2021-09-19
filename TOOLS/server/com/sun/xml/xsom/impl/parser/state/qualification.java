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
/*     */ class qualification
/*     */   extends NGCCHandler
/*     */ {
/*     */   private String text;
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
/*     */   public qualification(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public qualification(NGCCRuntimeEx runtime) {
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
/*  49 */         revertToParentFromEnterElement(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname, $attrs);
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
/*  68 */         revertToParentFromLeaveElement(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
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
/*  87 */         revertToParentFromEnterAttribute(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
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
/* 106 */         revertToParentFromLeaveAttribute(new Boolean(this.text.trim().equals("qualified")), this._cookie, $__uri, $__local, $__qname);
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
/* 122 */         revertToParentFromText(new Boolean(this.text.trim().equals("qualified")), this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 127 */         if ($value.equals("qualified")) {
/* 128 */           this.text = $value;
/* 129 */           this.$_ngcc_current_state = 0;
/*     */           break;
/*     */         } 
/* 132 */         if ($value.equals("unqualified")) {
/* 133 */           this.text = $value;
/* 134 */           this.$_ngcc_current_state = 0;
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 148 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\state\qualification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
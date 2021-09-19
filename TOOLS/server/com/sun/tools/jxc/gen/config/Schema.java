/*     */ package com.sun.tools.jxc.gen.config;
/*     */ 
/*     */ import com.sun.tools.jxc.NGCCRuntimeEx;
/*     */ import java.io.File;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class Schema extends NGCCHandler {
/*     */   private File baseDir;
/*     */   private String loc;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   private File location;
/*     */   private String namespace;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  20 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public Schema(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie, File _baseDir) {
/*  24 */     super(source, parent, cookie);
/*  25 */     this.$runtime = runtime;
/*  26 */     this.baseDir = _baseDir;
/*  27 */     this.$_ngcc_current_state = 10;
/*     */   }
/*     */   
/*     */   public Schema(NGCCRuntimeEx runtime, File _baseDir) {
/*  31 */     this((NGCCHandler)null, (NGCCEventSource)runtime, runtime, -1, _baseDir);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  35 */     this.location = new File(this.baseDir, this.loc);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*     */     int $ai;
/*  40 */     this.$uri = $__uri;
/*  41 */     this.$localName = $__local;
/*  42 */     this.$qname = $__qname;
/*  43 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 10:
/*  46 */         if ($__uri == "" && $__local == "schema") {
/*  47 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  48 */           this.$_ngcc_current_state = 6;
/*     */         } else {
/*     */           
/*  51 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 6:
/*  57 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/*  58 */           this.$runtime.consumeAttribute($ai);
/*  59 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  62 */           this.$_ngcc_current_state = 2;
/*  63 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/*  69 */         revertToParentFromEnterElement(this, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  74 */         if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/*  75 */           this.$runtime.consumeAttribute($ai);
/*  76 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  79 */           this.$_ngcc_current_state = 1;
/*  80 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/*  86 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/*     */     int $ai;
/*  94 */     this.$uri = $__uri;
/*  95 */     this.$localName = $__local;
/*  96 */     this.$qname = $__qname;
/*  97 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 100 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 101 */           this.$runtime.consumeAttribute($ai);
/* 102 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 105 */           this.$_ngcc_current_state = 2;
/* 106 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 112 */         revertToParentFromLeaveElement(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 117 */         if ($__uri == "" && $__local == "schema") {
/* 118 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 119 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 122 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 128 */         if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/* 129 */           this.$runtime.consumeAttribute($ai);
/* 130 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } else {
/*     */           
/* 133 */           this.$_ngcc_current_state = 1;
/* 134 */           this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 140 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 147 */     this.$uri = $__uri;
/* 148 */     this.$localName = $__local;
/* 149 */     this.$qname = $__qname;
/* 150 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 153 */         if ($__uri == "" && $__local == "namespace") {
/* 154 */           this.$_ngcc_current_state = 8;
/*     */         } else {
/*     */           
/* 157 */           this.$_ngcc_current_state = 2;
/* 158 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 164 */         revertToParentFromEnterAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 169 */         if ($__uri == "" && $__local == "location") {
/* 170 */           this.$_ngcc_current_state = 4;
/*     */         } else {
/*     */           
/* 173 */           this.$_ngcc_current_state = 1;
/* 174 */           this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 180 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 187 */     this.$uri = $__uri;
/* 188 */     this.$localName = $__local;
/* 189 */     this.$qname = $__qname;
/* 190 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 193 */         this.$_ngcc_current_state = 2;
/* 194 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 199 */         revertToParentFromLeaveAttribute(this, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 204 */         this.$_ngcc_current_state = 1;
/* 205 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 7:
/* 210 */         if ($__uri == "" && $__local == "namespace") {
/* 211 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 214 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/* 220 */         if ($__uri == "" && $__local == "location") {
/* 221 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 224 */           unexpectedLeaveAttribute($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 230 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/*     */     int $ai;
/* 238 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 6:
/* 241 */         if (($ai = this.$runtime.getAttributeIndex("", "namespace")) >= 0) {
/* 242 */           this.$runtime.consumeAttribute($ai);
/* 243 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 246 */         this.$_ngcc_current_state = 2;
/* 247 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 253 */         revertToParentFromText(this, this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 8:
/* 258 */         this.namespace = $value;
/* 259 */         this.$_ngcc_current_state = 7;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 264 */         if (($ai = this.$runtime.getAttributeIndex("", "location")) >= 0) {
/* 265 */           this.$runtime.consumeAttribute($ai);
/* 266 */           this.$runtime.sendText(this._cookie, $value);
/*     */           break;
/*     */         } 
/* 269 */         this.$_ngcc_current_state = 1;
/* 270 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 276 */         this.loc = $value;
/* 277 */         this.$_ngcc_current_state = 3;
/* 278 */         action0();
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
/* 290 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNamespace() {
/* 296 */     return this.namespace; } public File getLocation() {
/* 297 */     return this.location;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\gen\config\Schema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
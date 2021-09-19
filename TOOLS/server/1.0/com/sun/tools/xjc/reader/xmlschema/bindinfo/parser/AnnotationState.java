/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.declaration;
/*     */ import com.sun.xml.bind.marshaller.DataWriter;
/*     */ import java.io.StringWriter;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class AnnotationState
/*     */   extends NGCCHandler
/*     */ {
/*     */   private BIDeclaration result;
/*     */   private String msg;
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   protected String $localName;
/*     */   protected String $qname;
/*     */   public BindInfo bi;
/*     */   private StringWriter w;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  32 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   
/*     */   public AnnotationState(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  36 */     super(source, parent, cookie);
/*  37 */     this.$runtime = runtime;
/*  38 */     this.$_ngcc_current_state = 20;
/*     */   }
/*     */   
/*     */   public AnnotationState(NGCCRuntimeEx runtime) {
/*  42 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   private void action0() throws SAXException {
/*  46 */     this.bi.appendDocumentation("<pre>" + this.$runtime.escapeMarkup(this.$runtime.truncateDocComment(this.w.toString())) + "</pre>", false);
/*     */ 
/*     */ 
/*     */     
/*  50 */     this.w = null;
/*     */   }
/*     */   
/*     */   private void action1() throws SAXException {
/*  54 */     this.w = new StringWriter();
/*  55 */     DataWriter xw = new DataWriter(this.w, "UTF-8");
/*  56 */     xw.setXmlDecl(false);
/*  57 */     this.$runtime.redirectSubtree((ContentHandler)xw, this.$uri, this.$localName, this.$qname);
/*     */   }
/*     */   
/*     */   private void action2() throws SAXException {
/*  61 */     this.bi.appendDocumentation(this.$runtime.truncateDocComment(this.msg), true);
/*     */   }
/*     */   
/*     */   private void action3() throws SAXException {
/*  65 */     this.$runtime.redirectSubtree(new DefaultHandler(), this.$uri, this.$localName, this.$qname);
/*     */   }
/*     */ 
/*     */   
/*     */   private void action4() throws SAXException {
/*  70 */     this.bi.addDecl(this.result);
/*     */   }
/*     */   
/*     */   private void action5() throws SAXException {
/*  74 */     this.bi = new BindInfo(this.$runtime.copyLocator());
/*  75 */     this.$runtime.currentBindInfo = this.bi;
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  79 */     this.$uri = $__uri;
/*  80 */     this.$localName = $__local;
/*  81 */     this.$qname = $__qname;
/*  82 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/*  85 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "dom") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumClass") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "globalBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType")) {
/*  86 */           declaration declaration = new declaration(this, this._source, this.$runtime, 343);
/*  87 */           spawnChildFromEnterElement((NGCCEventReceiver)declaration, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  90 */         else if (!$__uri.equals("http://java.sun.com/xml/ns/jaxb/xjc") && !$__uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/*  91 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/*  92 */           action3();
/*  93 */           this.$_ngcc_current_state = 16;
/*     */         } else {
/*     */           
/*  96 */           this.$_ngcc_current_state = 11;
/*  97 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 104 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "appinfo") {
/* 105 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 106 */           this.$_ngcc_current_state = 12;
/*     */         
/*     */         }
/* 109 */         else if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "documentation") {
/* 110 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 111 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 114 */           this.$_ngcc_current_state = 1;
/* 115 */           this.$runtime.sendEnterElement(this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 11:
/* 122 */         if (($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "dom") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumClass") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") || ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "globalBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") || ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType")) {
/* 123 */           declaration declaration = new declaration(this, this._source, this.$runtime, 340);
/* 124 */           spawnChildFromEnterElement((NGCCEventReceiver)declaration, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/* 127 */         else if (!$__uri.equals("http://java.sun.com/xml/ns/jaxb/xjc") && !$__uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 128 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 129 */           action3();
/* 130 */           this.$_ngcc_current_state = 16;
/*     */         } else {
/*     */           
/* 133 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 141 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 142 */         action1();
/* 143 */         this.$_ngcc_current_state = 7;
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 5:
/* 153 */         this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 154 */         action1();
/* 155 */         this.$_ngcc_current_state = 7;
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 20:
/* 165 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "annotation") {
/* 166 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 167 */           action5();
/* 168 */           this.$_ngcc_current_state = 2;
/*     */         } else {
/*     */           
/* 171 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 177 */         revertToParentFromEnterElement(this.bi, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 182 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "appinfo") {
/* 183 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 184 */           this.$_ngcc_current_state = 12;
/*     */         
/*     */         }
/* 187 */         else if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "documentation") {
/* 188 */           this.$runtime.onEnterElementConsumed($__uri, $__local, $__qname, $attrs);
/* 189 */           this.$_ngcc_current_state = 5;
/*     */         } else {
/*     */           
/* 192 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 199 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/* 206 */     this.$uri = $__uri;
/* 207 */     this.$localName = $__local;
/* 208 */     this.$qname = $__qname;
/* 209 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 212 */         this.$_ngcc_current_state = 11;
/* 213 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 218 */         this.$_ngcc_current_state = 1;
/* 219 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 11:
/* 224 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "appinfo") {
/* 225 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 226 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 229 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 7:
/* 236 */         this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 237 */         this.$_ngcc_current_state = 4;
/* 238 */         action0();
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/* 247 */         if (!$__uri.equals("http://java.sun.com/xml/ns/jaxb/xjc") && !$__uri.equals("http://java.sun.com/xml/ns/jaxb")) {
/* 248 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 249 */           this.$_ngcc_current_state = 11;
/*     */         } else {
/*     */           
/* 252 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 4:
/* 258 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "documentation") {
/* 259 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 260 */           this.$_ngcc_current_state = 1;
/*     */         } else {
/*     */           
/* 263 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 269 */         this.$_ngcc_current_state = 4;
/* 270 */         this.$runtime.sendLeaveElement(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 275 */         revertToParentFromLeaveElement(this.bi, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 1:
/* 280 */         if ($__uri == "http://www.w3.org/2001/XMLSchema" && $__local == "annotation") {
/* 281 */           this.$runtime.onLeaveElementConsumed($__uri, $__local, $__qname);
/* 282 */           this.$_ngcc_current_state = 0;
/*     */         } else {
/*     */           
/* 285 */           unexpectedLeaveElement($__qname);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 291 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 298 */     this.$uri = $__uri;
/* 299 */     this.$localName = $__local;
/* 300 */     this.$qname = $__qname;
/* 301 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 304 */         this.$_ngcc_current_state = 11;
/* 305 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 310 */         this.$_ngcc_current_state = 1;
/* 311 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 316 */         this.$_ngcc_current_state = 4;
/* 317 */         this.$runtime.sendEnterAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 322 */         revertToParentFromEnterAttribute(this.bi, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 327 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 334 */     this.$uri = $__uri;
/* 335 */     this.$localName = $__local;
/* 336 */     this.$qname = $__qname;
/* 337 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 340 */         this.$_ngcc_current_state = 11;
/* 341 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 346 */         this.$_ngcc_current_state = 1;
/* 347 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 5:
/* 352 */         this.$_ngcc_current_state = 4;
/* 353 */         this.$runtime.sendLeaveAttribute(this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 358 */         revertToParentFromLeaveAttribute(this.bi, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 363 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 370 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 12:
/* 373 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 378 */         this.$_ngcc_current_state = 1;
/* 379 */         this.$runtime.sendText(this._cookie, $value);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 11:
/* 384 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 389 */         this.msg = $value;
/* 390 */         this.$_ngcc_current_state = 4;
/* 391 */         action2();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 396 */         this.msg = $value;
/* 397 */         this.$_ngcc_current_state = 4;
/* 398 */         action2();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/* 403 */         revertToParentFromText(this.bi, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 410 */     switch ($__cookie__) {
/*     */       
/*     */       case 340:
/* 413 */         this.result = (BIDeclaration)$__result__;
/* 414 */         action4();
/* 415 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 343:
/* 420 */         this.result = (BIDeclaration)$__result__;
/* 421 */         action4();
/* 422 */         this.$_ngcc_current_state = 11;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 429 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\AnnotationState.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
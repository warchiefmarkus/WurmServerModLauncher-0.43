/*     */ package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIDeclaration;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.NGCCRuntimeEx;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.BIClassState;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventReceiver;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCEventSource;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCHandler;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.NGCCRuntime;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.conversion;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.dom;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.enumDef;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.enumMember;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.globalBindings;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.idSymbolSpace;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.property;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.parser.schemaBindings;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class declaration
/*     */   extends NGCCHandler {
/*     */   protected final NGCCRuntimeEx $runtime;
/*     */   private int $_ngcc_current_state;
/*     */   protected String $uri;
/*     */   
/*     */   public final NGCCRuntime getRuntime() {
/*  28 */     return (NGCCRuntime)this.$runtime;
/*     */   }
/*     */   protected String $localName; protected String $qname; private BIDeclaration result;
/*     */   public declaration(NGCCHandler parent, NGCCEventSource source, NGCCRuntimeEx runtime, int cookie) {
/*  32 */     super(source, parent, cookie);
/*  33 */     this.$runtime = runtime;
/*  34 */     this.$_ngcc_current_state = 1;
/*     */   }
/*     */   
/*     */   public declaration(NGCCRuntimeEx runtime) {
/*  38 */     this(null, (NGCCEventSource)runtime, runtime, -1);
/*     */   }
/*     */   
/*     */   public void enterElement(String $__uri, String $__local, String $__qname, Attributes $attrs) throws SAXException {
/*  42 */     this.$uri = $__uri;
/*  43 */     this.$localName = $__local;
/*  44 */     this.$qname = $__qname;
/*  45 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 1:
/*  48 */         if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "globalBindings") {
/*  49 */           globalBindings globalBindings = new globalBindings(this, this._source, this.$runtime, 71);
/*  50 */           spawnChildFromEnterElement((NGCCEventReceiver)globalBindings, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  53 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "schemaBindings") {
/*  54 */           schemaBindings schemaBindings = new schemaBindings(this, this._source, this.$runtime, 72);
/*  55 */           spawnChildFromEnterElement((NGCCEventReceiver)schemaBindings, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  58 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "class") {
/*  59 */           BIClassState bIClassState = new BIClassState(this, this._source, this.$runtime, 73);
/*  60 */           spawnChildFromEnterElement((NGCCEventReceiver)bIClassState, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  63 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "javaType") {
/*  64 */           conversion conversion = new conversion(this, this._source, this.$runtime, 74);
/*  65 */           spawnChildFromEnterElement((NGCCEventReceiver)conversion, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  68 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "property") {
/*  69 */           property property = new property(this, this._source, this.$runtime, 75);
/*  70 */           spawnChildFromEnterElement((NGCCEventReceiver)property, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  73 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumClass") {
/*  74 */           enumDef enumDef = new enumDef(this, this._source, this.$runtime, 76);
/*  75 */           spawnChildFromEnterElement((NGCCEventReceiver)enumDef, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  78 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb" && $__local == "typesafeEnumMember") {
/*  79 */           enumMember enumMember = new enumMember(this, this._source, this.$runtime, 77);
/*  80 */           spawnChildFromEnterElement((NGCCEventReceiver)enumMember, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  83 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "idSymbolSpace") {
/*  84 */           idSymbolSpace idSymbolSpace = new idSymbolSpace(this, this._source, this.$runtime, 78);
/*  85 */           spawnChildFromEnterElement((NGCCEventReceiver)idSymbolSpace, $__uri, $__local, $__qname, $attrs);
/*     */         
/*     */         }
/*  88 */         else if ($__uri == "http://java.sun.com/xml/ns/jaxb/xjc" && $__local == "dom") {
/*  89 */           dom dom = new dom(this, this._source, this.$runtime, 27);
/*  90 */           spawnChildFromEnterElement((NGCCEventReceiver)dom, $__uri, $__local, $__qname, $attrs);
/*     */         } else {
/*     */           
/*  93 */           unexpectedEnterElement($__qname);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 107 */         revertToParentFromEnterElement(this.result, this._cookie, $__uri, $__local, $__qname, $attrs);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     unexpectedEnterElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveElement(String $__uri, String $__local, String $__qname) throws SAXException {
/* 119 */     this.$uri = $__uri;
/* 120 */     this.$localName = $__local;
/* 121 */     this.$qname = $__qname;
/* 122 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 125 */         revertToParentFromLeaveElement(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 130 */     unexpectedLeaveElement($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enterAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 137 */     this.$uri = $__uri;
/* 138 */     this.$localName = $__local;
/* 139 */     this.$qname = $__qname;
/* 140 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 143 */         revertToParentFromEnterAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 148 */     unexpectedEnterAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void leaveAttribute(String $__uri, String $__local, String $__qname) throws SAXException {
/* 155 */     this.$uri = $__uri;
/* 156 */     this.$localName = $__local;
/* 157 */     this.$qname = $__qname;
/* 158 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 161 */         revertToParentFromLeaveAttribute(this.result, this._cookie, $__uri, $__local, $__qname);
/*     */         return;
/*     */     } 
/*     */ 
/*     */     
/* 166 */     unexpectedLeaveAttribute($__qname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void text(String $value) throws SAXException {
/* 173 */     switch (this.$_ngcc_current_state) {
/*     */       
/*     */       case 0:
/* 176 */         revertToParentFromText(this.result, this._cookie, $value);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onChildCompleted(Object $__result__, int $__cookie__, boolean $__needAttCheck__) throws SAXException {
/* 183 */     switch ($__cookie__) {
/*     */       
/*     */       case 71:
/* 186 */         this.result = (BIDeclaration)$__result__;
/* 187 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 72:
/* 192 */         this.result = (BIDeclaration)$__result__;
/* 193 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 73:
/* 198 */         this.result = (BIDeclaration)$__result__;
/* 199 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 74:
/* 204 */         this.result = (BIDeclaration)$__result__;
/* 205 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 75:
/* 210 */         this.result = (BIDeclaration)$__result__;
/* 211 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 76:
/* 216 */         this.result = (BIDeclaration)$__result__;
/* 217 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 77:
/* 222 */         this.result = (BIDeclaration)$__result__;
/* 223 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 78:
/* 228 */         this.result = (BIDeclaration)$__result__;
/* 229 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 27:
/* 234 */         this.result = (BIDeclaration)$__result__;
/* 235 */         this.$_ngcc_current_state = 0;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean accepted() {
/* 242 */     return (this.$_ngcc_current_state == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\declaration.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
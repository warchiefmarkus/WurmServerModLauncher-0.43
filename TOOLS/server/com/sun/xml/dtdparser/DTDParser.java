/*      */ package com.sun.xml.dtdparser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DTDParser
/*      */ {
/*      */   public static final String TYPE_CDATA = "CDATA";
/*      */   public static final String TYPE_ID = "ID";
/*      */   public static final String TYPE_IDREF = "IDREF";
/*      */   public static final String TYPE_IDREFS = "IDREFS";
/*      */   public static final String TYPE_ENTITY = "ENTITY";
/*      */   public static final String TYPE_ENTITIES = "ENTITIES";
/*      */   public static final String TYPE_NMTOKEN = "NMTOKEN";
/*      */   public static final String TYPE_NMTOKENS = "NMTOKENS";
/*      */   public static final String TYPE_NOTATION = "NOTATION";
/*      */   public static final String TYPE_ENUMERATION = "ENUMERATION";
/*      */   private InputEntity in;
/*      */   private StringBuffer strTmp;
/*      */   private char[] nameTmp;
/*      */   private NameCache nameCache;
/*   64 */   private char[] charTmp = new char[2];
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean doLexicalPE;
/*      */ 
/*      */   
/*   71 */   protected final Set declaredElements = new HashSet();
/*   72 */   private SimpleHashtable params = new SimpleHashtable(7);
/*      */ 
/*      */   
/*   75 */   Hashtable notations = new Hashtable(7);
/*   76 */   SimpleHashtable entities = new SimpleHashtable(17);
/*      */   
/*   78 */   private SimpleHashtable ids = new SimpleHashtable();
/*      */ 
/*      */   
/*      */   private DTDEventListener dtdHandler;
/*      */ 
/*      */   
/*      */   private EntityResolver resolver;
/*      */ 
/*      */   
/*      */   private Locale locale;
/*      */ 
/*      */   
/*      */   static final String strANY = "ANY";
/*      */ 
/*      */   
/*      */   static final String strEMPTY = "EMPTY";
/*      */   
/*      */   private static final String XmlLang = "xml:lang";
/*      */ 
/*      */   
/*      */   public void setLocale(Locale l) throws SAXException {
/*   99 */     if (l != null && !messages.isLocaleSupported(l.toString())) {
/*  100 */       throw new SAXException(messages.getMessage(this.locale, "P-078", new Object[] { l }));
/*      */     }
/*      */     
/*  103 */     this.locale = l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  110 */     return this.locale;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale chooseLocale(String[] languages) throws SAXException {
/*  130 */     Locale l = messages.chooseLocale(languages);
/*      */     
/*  132 */     if (l != null) {
/*  133 */       setLocale(l);
/*      */     }
/*  135 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEntityResolver(EntityResolver r) {
/*  143 */     this.resolver = r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityResolver getEntityResolver() {
/*  151 */     return this.resolver;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDtdHandler(DTDEventListener handler) {
/*  158 */     this.dtdHandler = handler;
/*  159 */     if (handler != null) {
/*  160 */       handler.setDocumentLocator(new Locator(this) { private final DTDParser this$0;
/*      */             public String getPublicId() {
/*  162 */               return this.this$0.getPublicId();
/*      */             }
/*      */             
/*      */             public String getSystemId() {
/*  166 */               return this.this$0.getSystemId();
/*      */             }
/*      */             
/*      */             public int getLineNumber() {
/*  170 */               return this.this$0.getLineNumber();
/*      */             }
/*      */             
/*      */             public int getColumnNumber() {
/*  174 */               return this.this$0.getColumnNumber();
/*      */             } }
/*      */         );
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public DTDEventListener getDtdHandler() {
/*  183 */     return this.dtdHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parse(InputSource in) throws IOException, SAXException {
/*  191 */     init();
/*  192 */     parseInternal(in);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void parse(String uri) throws IOException, SAXException {
/*  202 */     init();
/*      */     
/*  204 */     InputSource in = this.resolver.resolveEntity(null, uri);
/*      */ 
/*      */     
/*  207 */     if (in == null) {
/*  208 */       in = Resolver.createInputSource(new URL(uri), false);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  213 */     else if (in.getSystemId() == null) {
/*  214 */       warning("P-065", null);
/*  215 */       in.setSystemId(uri);
/*      */     } 
/*      */     
/*  218 */     parseInternal(in);
/*      */   }
/*      */ 
/*      */   
/*      */   private void init() {
/*  223 */     this.in = null;
/*      */ 
/*      */     
/*  226 */     this.strTmp = new StringBuffer();
/*  227 */     this.nameTmp = new char[20];
/*  228 */     this.nameCache = new NameCache();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  233 */     this.doLexicalPE = false;
/*      */     
/*  235 */     this.entities.clear();
/*  236 */     this.notations.clear();
/*  237 */     this.params.clear();
/*      */     
/*  239 */     this.declaredElements.clear();
/*      */ 
/*      */     
/*  242 */     builtin("amp", "&#38;");
/*  243 */     builtin("lt", "&#60;");
/*  244 */     builtin("gt", ">");
/*  245 */     builtin("quot", "\"");
/*  246 */     builtin("apos", "'");
/*      */     
/*  248 */     if (this.locale == null)
/*  249 */       this.locale = Locale.getDefault(); 
/*  250 */     if (this.resolver == null)
/*  251 */       this.resolver = new Resolver(); 
/*  252 */     if (this.dtdHandler == null) {
/*  253 */       this.dtdHandler = new DTDHandlerBase();
/*      */     }
/*      */   }
/*      */   
/*      */   private void builtin(String entityName, String entityValue) {
/*  258 */     InternalEntity entity = new InternalEntity(entityName, entityValue.toCharArray());
/*  259 */     this.entities.put(entityName, entity);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseInternal(InputSource input) throws IOException, SAXException {
/*  279 */     if (input == null) {
/*  280 */       fatal("P-000");
/*      */     }
/*      */     try {
/*  283 */       this.in = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/*  284 */       this.in.init(input, (String)null, (InputEntity)null, false);
/*      */       
/*  286 */       this.dtdHandler.startDTD(this.in);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  293 */       ExternalEntity externalSubset = new ExternalEntity(this.in);
/*  294 */       externalParameterEntity(externalSubset);
/*      */       
/*  296 */       if (!this.in.isEOF()) {
/*  297 */         fatal("P-001", new Object[] { Integer.toHexString(getc()) });
/*      */       }
/*      */       
/*  300 */       afterRoot();
/*  301 */       this.dtdHandler.endDTD();
/*      */     }
/*  303 */     catch (EndOfInputException e) {
/*  304 */       if (!this.in.isDocument()) {
/*  305 */         String name = this.in.getName();
/*      */         while (true) {
/*  307 */           this.in = this.in.pop();
/*  308 */           if (!this.in.isInternal())
/*  309 */           { fatal("P-002", new Object[] { name }); return; } 
/*      */         } 
/*  311 */       }  fatal("P-003", null);
/*      */     }
/*  313 */     catch (RuntimeException e) {
/*      */ 
/*      */       
/*  316 */       System.err.print("Internal DTD parser error: ");
/*  317 */       e.printStackTrace();
/*  318 */       throw new SAXParseException((e.getMessage() != null) ? e.getMessage() : e.getClass().getName(), getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */ 
/*      */       
/*  325 */       this.strTmp = null;
/*  326 */       this.nameTmp = null;
/*  327 */       this.nameCache = null;
/*      */ 
/*      */       
/*  330 */       if (this.in != null) {
/*  331 */         this.in.close();
/*  332 */         this.in = null;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  338 */       this.params.clear();
/*  339 */       this.entities.clear();
/*  340 */       this.notations.clear();
/*  341 */       this.declaredElements.clear();
/*      */       
/*  343 */       this.ids.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void afterRoot() throws SAXException {
/*  352 */     Enumeration e = this.ids.keys();
/*  353 */     while (e.hasMoreElements()) {
/*      */       
/*  355 */       String id = e.nextElement();
/*  356 */       Boolean value = (Boolean)this.ids.get(id);
/*  357 */       if (Boolean.FALSE == value) {
/*  358 */         error("V-024", new Object[] { id });
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void whitespace(String roleId) throws IOException, SAXException {
/*  368 */     if (!maybeWhitespace()) {
/*  369 */       fatal("P-004", new Object[] { messages.getMessage(this.locale, roleId) });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeWhitespace() throws IOException, SAXException {
/*  378 */     if (!this.doLexicalPE) {
/*  379 */       return this.in.maybeWhitespace();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  394 */     char c = getc();
/*  395 */     boolean saw = false;
/*      */     
/*  397 */     while (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
/*  398 */       saw = true;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  403 */       if (this.in.isEOF() && !this.in.isInternal())
/*  404 */         return saw; 
/*  405 */       c = getc();
/*      */     } 
/*  407 */     ungetc();
/*  408 */     return saw;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String maybeGetName() throws IOException, SAXException {
/*  414 */     NameCacheEntry entry = maybeGetNameCacheEntry();
/*  415 */     return (entry == null) ? null : entry.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NameCacheEntry maybeGetNameCacheEntry() throws IOException, SAXException {
/*  422 */     char c = getc();
/*      */     
/*  424 */     if (!XmlChars.isLetter(c) && c != ':' && c != '_') {
/*  425 */       ungetc();
/*  426 */       return null;
/*      */     } 
/*  428 */     return nameCharString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getNmtoken() throws IOException, SAXException {
/*  436 */     char c = getc();
/*  437 */     if (!XmlChars.isNameChar(c))
/*  438 */       fatal("P-006", new Object[] { new Character(c) }); 
/*  439 */     return (nameCharString(c)).name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NameCacheEntry nameCharString(char c) throws IOException, SAXException {
/*  450 */     int i = 1;
/*      */     
/*  452 */     this.nameTmp[0] = c;
/*      */     
/*  454 */     while ((c = this.in.getNameChar()) != '\000') {
/*      */       
/*  456 */       if (i >= this.nameTmp.length) {
/*  457 */         char[] tmp = new char[this.nameTmp.length + 10];
/*  458 */         System.arraycopy(this.nameTmp, 0, tmp, 0, this.nameTmp.length);
/*  459 */         this.nameTmp = tmp;
/*      */       } 
/*  461 */       this.nameTmp[i++] = c;
/*      */     } 
/*  463 */     return this.nameCache.lookupEntry(this.nameTmp, i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void parseLiteral(boolean isEntityValue) throws IOException, SAXException {
/*  484 */     char quote = getc();
/*      */     
/*  486 */     InputEntity source = this.in;
/*      */     
/*  488 */     if (quote != '\'' && quote != '"') {
/*  489 */       fatal("P-007");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  497 */     this.strTmp = new StringBuffer();
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  502 */       if (this.in != source && this.in.isEOF()) {
/*      */ 
/*      */         
/*  505 */         this.in = this.in.pop(); continue;
/*      */       } 
/*      */       char c;
/*  508 */       if ((c = getc()) == quote && this.in == source) {
/*      */         break;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  516 */       if (c == '&') {
/*  517 */         String entityName = maybeGetName();
/*      */         
/*  519 */         if (entityName != null) {
/*  520 */           nextChar(';', "F-020", entityName);
/*      */ 
/*      */ 
/*      */           
/*  524 */           if (isEntityValue) {
/*  525 */             this.strTmp.append('&');
/*  526 */             this.strTmp.append(entityName);
/*  527 */             this.strTmp.append(';');
/*      */             continue;
/*      */           } 
/*  530 */           expandEntityInLiteral(entityName, this.entities, isEntityValue);
/*      */           
/*      */           continue;
/*      */         } 
/*  534 */         if ((c = getc()) == '#') {
/*  535 */           int tmp = parseCharNumber();
/*      */           
/*  537 */           if (tmp > 65535) {
/*  538 */             tmp = surrogatesToCharTmp(tmp);
/*  539 */             this.strTmp.append(this.charTmp[0]);
/*  540 */             if (tmp == 2)
/*  541 */               this.strTmp.append(this.charTmp[1]);  continue;
/*      */           } 
/*  543 */           this.strTmp.append((char)tmp); continue;
/*      */         } 
/*  545 */         fatal("P-009");
/*      */ 
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  551 */       if (c == '%' && isEntityValue) {
/*  552 */         String entityName = maybeGetName();
/*      */         
/*  554 */         if (entityName != null) {
/*  555 */           nextChar(';', "F-021", entityName);
/*  556 */           expandEntityInLiteral(entityName, this.params, isEntityValue);
/*      */           continue;
/*      */         } 
/*  559 */         fatal("P-011");
/*      */       } 
/*      */ 
/*      */       
/*  563 */       if (!isEntityValue) {
/*      */         
/*  565 */         if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
/*  566 */           this.strTmp.append(' ');
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  571 */         if (c == '<') {
/*  572 */           fatal("P-012");
/*      */         }
/*      */       } 
/*  575 */       this.strTmp.append(c);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expandEntityInLiteral(String name, SimpleHashtable table, boolean isEntityValue) throws IOException, SAXException {
/*  585 */     Object entity = table.get(name);
/*      */     
/*  587 */     if (entity instanceof InternalEntity) {
/*  588 */       InternalEntity value = (InternalEntity)entity;
/*  589 */       pushReader(value.buf, name, !value.isPE);
/*      */     }
/*  591 */     else if (entity instanceof ExternalEntity) {
/*  592 */       if (!isEntityValue) {
/*  593 */         fatal("P-013", new Object[] { name });
/*      */       }
/*  595 */       pushReader((ExternalEntity)entity);
/*      */     }
/*  597 */     else if (entity == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  603 */       fatal((table == this.params) ? "V-022" : "P-014", new Object[] { name });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getQuotedString(String type, String extra) throws IOException, SAXException {
/*  620 */     char quote = this.in.getc();
/*      */     
/*  622 */     if (quote != '\'' && quote != '"') {
/*  623 */       fatal("P-015", new Object[] { messages.getMessage(this.locale, type, new Object[] { extra }) });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  629 */     this.strTmp = new StringBuffer(); char c;
/*  630 */     while ((c = this.in.getc()) != quote)
/*  631 */       this.strTmp.append(c); 
/*  632 */     return this.strTmp.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String parsePublicId() throws IOException, SAXException {
/*  640 */     String retval = getQuotedString("F-033", null);
/*  641 */     for (int i = 0; i < retval.length(); i++) {
/*  642 */       char c = retval.charAt(i);
/*  643 */       if (" \r\n-'()+,./:=?;!*#@$_%0123456789".indexOf(c) == -1 && (c < 'A' || c > 'Z') && (c < 'a' || c > 'z'))
/*      */       {
/*      */         
/*  646 */         fatal("P-016", new Object[] { new Character(c) }); } 
/*      */     } 
/*  648 */     this.strTmp = new StringBuffer();
/*  649 */     this.strTmp.append(retval);
/*  650 */     return normalize(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeComment(boolean skipStart) throws IOException, SAXException {
/*  662 */     if (!this.in.peek(skipStart ? "!--" : "<!--", null)) {
/*  663 */       return false;
/*      */     }
/*  665 */     boolean savedLexicalPE = this.doLexicalPE;
/*      */ 
/*      */     
/*  668 */     this.doLexicalPE = false;
/*  669 */     boolean saveCommentText = false;
/*  670 */     if (saveCommentText) {
/*  671 */       this.strTmp = new StringBuffer();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/*  679 */         int c = getc();
/*  680 */         if (c == 45) {
/*  681 */           c = getc();
/*  682 */           if (c != 45) {
/*  683 */             if (saveCommentText)
/*  684 */               this.strTmp.append('-'); 
/*  685 */             ungetc();
/*      */             continue;
/*      */           } 
/*  688 */           nextChar('>', "F-022", null);
/*      */           break;
/*      */         } 
/*  691 */         if (saveCommentText) {
/*  692 */           this.strTmp.append((char)c);
/*      */         }
/*  694 */       } catch (EndOfInputException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  701 */         if (this.in.isInternal()) {
/*  702 */           error("V-021", null);
/*      */         }
/*  704 */         fatal("P-017");
/*      */       } 
/*      */     } 
/*  707 */     this.doLexicalPE = savedLexicalPE;
/*  708 */     if (saveCommentText)
/*  709 */       this.dtdHandler.comment(this.strTmp.toString()); 
/*  710 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybePI(boolean skipStart) throws IOException, SAXException {
/*  720 */     boolean savedLexicalPE = this.doLexicalPE;
/*      */     
/*  722 */     if (!this.in.peek(skipStart ? "?" : "<?", null))
/*  723 */       return false; 
/*  724 */     this.doLexicalPE = false;
/*      */     
/*  726 */     String target = maybeGetName();
/*      */     
/*  728 */     if (target == null) {
/*  729 */       fatal("P-018");
/*      */     }
/*  731 */     if ("xml".equals(target)) {
/*  732 */       fatal("P-019");
/*      */     }
/*  734 */     if ("xml".equalsIgnoreCase(target)) {
/*  735 */       fatal("P-020", new Object[] { target });
/*      */     }
/*      */     
/*  738 */     if (maybeWhitespace()) {
/*  739 */       this.strTmp = new StringBuffer();
/*      */       
/*      */       try {
/*      */         while (true) {
/*  743 */           char c = this.in.getc();
/*      */           
/*  745 */           if (c == '?' && this.in.peekc('>'))
/*      */             break; 
/*  747 */           this.strTmp.append(c);
/*      */         } 
/*  749 */       } catch (EndOfInputException e) {
/*  750 */         fatal("P-021");
/*      */       } 
/*  752 */       this.dtdHandler.processingInstruction(target, this.strTmp.toString());
/*      */     } else {
/*  754 */       if (!this.in.peek("?>", null)) {
/*  755 */         fatal("P-022");
/*      */       }
/*  757 */       this.dtdHandler.processingInstruction(target, "");
/*      */     } 
/*      */     
/*  760 */     this.doLexicalPE = savedLexicalPE;
/*  761 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String maybeReadAttribute(String name, boolean must) throws IOException, SAXException {
/*  779 */     if (!maybeWhitespace()) {
/*  780 */       if (!must) {
/*  781 */         return null;
/*      */       }
/*  783 */       fatal("P-024", new Object[] { name });
/*      */     } 
/*      */ 
/*      */     
/*  787 */     if (!peek(name)) {
/*  788 */       if (must) {
/*  789 */         fatal("P-024", new Object[] { name });
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  794 */         ungetc();
/*  795 */         return null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  800 */     maybeWhitespace();
/*  801 */     nextChar('=', "F-023", null);
/*  802 */     maybeWhitespace();
/*      */     
/*  804 */     return getQuotedString("F-035", name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void readVersion(boolean must, String versionNum) throws IOException, SAXException {
/*  810 */     String value = maybeReadAttribute("version", must);
/*      */ 
/*      */ 
/*      */     
/*  814 */     if (must && value == null)
/*  815 */       fatal("P-025", new Object[] { versionNum }); 
/*  816 */     if (value != null) {
/*  817 */       int length = value.length();
/*  818 */       for (int i = 0; i < length; i++) {
/*  819 */         char c = value.charAt(i);
/*  820 */         if ((c < '0' || c > '9') && c != '_' && c != '.' && (c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && c != ':' && c != '-')
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  826 */           fatal("P-026", new Object[] { value }); } 
/*      */       } 
/*      */     } 
/*  829 */     if (value != null && !value.equals(versionNum)) {
/*  830 */       error("P-027", new Object[] { versionNum, value });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getMarkupDeclname(String roleId, boolean qname) throws IOException, SAXException {
/*  840 */     whitespace(roleId);
/*  841 */     String name = maybeGetName();
/*  842 */     if (name == null) {
/*  843 */       fatal("P-005", new Object[] { messages.getMessage(this.locale, roleId) });
/*      */     }
/*  845 */     return name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeMarkupDecl() throws IOException, SAXException {
/*  853 */     return (maybeElementDecl() || maybeAttlistDecl() || maybeEntityDecl() || maybeNotationDecl() || maybePI(false) || maybeComment(false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isXmlLang(String value) {
/*      */     int nextSuffix;
/*  878 */     if (value.length() < 2)
/*  879 */       return false; 
/*  880 */     char c = value.charAt(1);
/*  881 */     if (c == '-') {
/*  882 */       c = value.charAt(0);
/*  883 */       if (c != 'i' && c != 'I' && c != 'x' && c != 'X')
/*  884 */         return false; 
/*  885 */       nextSuffix = 1;
/*  886 */     } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
/*      */       
/*  888 */       c = value.charAt(0);
/*  889 */       if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
/*  890 */         return false; 
/*  891 */       nextSuffix = 2;
/*      */     } else {
/*  893 */       return false;
/*      */     } 
/*      */     
/*  896 */     while (nextSuffix < value.length()) {
/*  897 */       c = value.charAt(nextSuffix);
/*  898 */       if (c != '-')
/*      */         break; 
/*  900 */       while (++nextSuffix < value.length()) {
/*  901 */         c = value.charAt(nextSuffix);
/*  902 */         if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z'))
/*      */           break; 
/*      */       } 
/*      */     } 
/*  906 */     return (value.length() == nextSuffix && c != '-');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeElementDecl() throws IOException, SAXException {
/*      */     short modelType;
/* 1045 */     InputEntity start = peekDeclaration("!ELEMENT");
/*      */     
/* 1047 */     if (start == null) {
/* 1048 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1052 */     String name = getMarkupDeclname("F-015", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1067 */     if (this.declaredElements.contains(name)) {
/* 1068 */       error("V-012", new Object[] { name });
/*      */     } else {
/* 1070 */       this.declaredElements.add(name);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1075 */     whitespace("F-000");
/* 1076 */     if (peek("EMPTY")) {
/*      */       
/* 1078 */       this.dtdHandler.startContentModel(name, modelType = 0);
/* 1079 */     } else if (peek("ANY")) {
/*      */       
/* 1081 */       this.dtdHandler.startContentModel(name, modelType = 1);
/*      */     } else {
/* 1083 */       modelType = getMixedOrChildren(name);
/*      */     } 
/*      */     
/* 1086 */     this.dtdHandler.endContentModel(name, modelType);
/*      */     
/* 1088 */     maybeWhitespace();
/* 1089 */     char c = getc();
/* 1090 */     if (c != '>')
/* 1091 */       fatal("P-036", new Object[] { name, new Character(c) }); 
/* 1092 */     if (start != this.in) {
/* 1093 */       error("V-013", null);
/*      */     }
/*      */ 
/*      */     
/* 1097 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private short getMixedOrChildren(String elementName) throws IOException, SAXException {
/*      */     short modelType;
/* 1114 */     this.strTmp = new StringBuffer();
/*      */     
/* 1116 */     nextChar('(', "F-028", elementName);
/* 1117 */     InputEntity start = this.in;
/* 1118 */     maybeWhitespace();
/* 1119 */     this.strTmp.append('(');
/*      */ 
/*      */     
/* 1122 */     if (peek("#PCDATA")) {
/* 1123 */       this.strTmp.append("#PCDATA");
/* 1124 */       this.dtdHandler.startContentModel(elementName, modelType = 2);
/* 1125 */       getMixed(elementName, start);
/*      */     } else {
/* 1127 */       this.dtdHandler.startContentModel(elementName, modelType = 3);
/* 1128 */       getcps(elementName, start);
/*      */     } 
/*      */     
/* 1131 */     return modelType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getcps(String elementName, InputEntity start) throws IOException, SAXException {
/* 1142 */     boolean decided = false;
/* 1143 */     char type = Character.MIN_VALUE;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1148 */     this.dtdHandler.startModelGroup();
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1153 */       String tag = maybeGetName();
/* 1154 */       if (tag != null) {
/* 1155 */         this.strTmp.append(tag);
/*      */ 
/*      */ 
/*      */         
/* 1159 */         this.dtdHandler.childElement(tag, getFrequency());
/*      */       }
/* 1161 */       else if (peek("(")) {
/* 1162 */         InputEntity next = this.in;
/* 1163 */         this.strTmp.append('(');
/* 1164 */         maybeWhitespace();
/*      */ 
/*      */ 
/*      */         
/* 1168 */         getcps(elementName, next);
/*      */       }
/*      */       else {
/*      */         
/* 1172 */         fatal((type == '\000') ? "P-039" : ((type == ',') ? "P-037" : "P-038"), new Object[] { new Character(getc()) });
/*      */       } 
/*      */ 
/*      */       
/* 1176 */       maybeWhitespace();
/* 1177 */       if (decided)
/* 1178 */       { char c = getc();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1183 */         if (c == type)
/* 1184 */         { this.strTmp.append(type);
/* 1185 */           maybeWhitespace();
/* 1186 */           reportConnector(type); }
/*      */         
/* 1188 */         else if (c == ')')
/* 1189 */         { ungetc(); }
/*      */         else
/*      */         
/* 1192 */         { fatal((type == '\000') ? "P-041" : "P-040", new Object[] { new Character(c), new Character(type) });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1215 */           maybeWhitespace(); }  } else { type = getc(); switch (type) { case ',': case '|': reportConnector(type); break;
/* 1216 */           default: ungetc(); if (peek(")")) break;  continue; }  decided = true; this.strTmp.append(type); maybeWhitespace(); }  if (peek(")"))
/*      */         break; 
/* 1218 */     }  if (this.in != start)
/* 1219 */       error("V-014", new Object[] { elementName }); 
/* 1220 */     this.strTmp.append(')');
/*      */     
/* 1222 */     this.dtdHandler.endModelGroup(getFrequency());
/*      */   }
/*      */ 
/*      */   
/*      */   private void reportConnector(char type) throws SAXException {
/* 1227 */     switch (type) {
/*      */       case '|':
/* 1229 */         this.dtdHandler.connector((short)0);
/*      */         return;
/*      */       case ',':
/* 1232 */         this.dtdHandler.connector((short)1);
/*      */         return;
/*      */     } 
/* 1235 */     throw new Error();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private short getFrequency() throws IOException, SAXException {
/* 1242 */     char c = getc();
/*      */     
/* 1244 */     if (c == '?') {
/* 1245 */       this.strTmp.append(c);
/* 1246 */       return 2;
/*      */     } 
/* 1248 */     if (c == '+') {
/* 1249 */       this.strTmp.append(c);
/* 1250 */       return 1;
/*      */     } 
/* 1252 */     if (c == '*') {
/* 1253 */       this.strTmp.append(c);
/* 1254 */       return 0;
/*      */     } 
/*      */     
/* 1257 */     ungetc();
/* 1258 */     return 3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void getMixed(String elementName, InputEntity start) throws IOException, SAXException {
/* 1269 */     maybeWhitespace();
/* 1270 */     if (peek(")*") || peek(")")) {
/* 1271 */       if (this.in != start)
/* 1272 */         error("V-014", new Object[] { elementName }); 
/* 1273 */       this.strTmp.append(')');
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1278 */     ArrayList l = new ArrayList();
/*      */ 
/*      */ 
/*      */     
/* 1282 */     while (peek("|")) {
/*      */ 
/*      */       
/* 1285 */       this.strTmp.append('|');
/* 1286 */       maybeWhitespace();
/*      */       
/* 1288 */       this.doLexicalPE = true;
/* 1289 */       String name = maybeGetName();
/* 1290 */       if (name == null) {
/* 1291 */         fatal("P-042", new Object[] { elementName, Integer.toHexString(getc()) });
/*      */       }
/* 1293 */       if (l.contains(name)) {
/* 1294 */         error("V-015", new Object[] { name });
/*      */       } else {
/* 1296 */         l.add(name);
/* 1297 */         this.dtdHandler.mixedElement(name);
/*      */       } 
/* 1299 */       this.strTmp.append(name);
/* 1300 */       maybeWhitespace();
/*      */     } 
/*      */     
/* 1303 */     if (!peek(")*")) {
/* 1304 */       fatal("P-043", new Object[] { elementName, new Character(getc()) });
/*      */     }
/* 1306 */     if (this.in != start)
/* 1307 */       error("V-014", new Object[] { elementName }); 
/* 1308 */     this.strTmp.append(')');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeAttlistDecl() throws IOException, SAXException {
/* 1318 */     InputEntity start = peekDeclaration("!ATTLIST");
/*      */     
/* 1320 */     if (start == null) {
/* 1321 */       return false;
/*      */     }
/* 1323 */     String elementName = getMarkupDeclname("F-016", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1332 */     while (!peek(">")) {
/*      */       String typeName;
/*      */ 
/*      */       
/*      */       short attributeUse;
/*      */       
/* 1338 */       maybeWhitespace();
/* 1339 */       char c = getc();
/* 1340 */       if (c == '%') {
/* 1341 */         String entityName = maybeGetName();
/* 1342 */         if (entityName != null) {
/* 1343 */           nextChar(';', "F-021", entityName);
/* 1344 */           whitespace("F-021");
/*      */           continue;
/*      */         } 
/* 1347 */         fatal("P-011");
/*      */       } 
/*      */       
/* 1350 */       ungetc();
/*      */       
/* 1352 */       String attName = maybeGetName();
/* 1353 */       if (attName == null) {
/* 1354 */         fatal("P-044", new Object[] { new Character(getc()) });
/*      */       }
/* 1356 */       whitespace("F-001");
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1361 */       Vector values = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1367 */       if (peek("CDATA")) {
/*      */         
/* 1369 */         typeName = "CDATA";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1376 */       else if (peek("IDREFS")) {
/* 1377 */         typeName = "IDREFS";
/* 1378 */       } else if (peek("IDREF")) {
/* 1379 */         typeName = "IDREF";
/* 1380 */       } else if (peek("ID")) {
/* 1381 */         typeName = "ID";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1387 */       else if (peek("ENTITY")) {
/* 1388 */         typeName = "ENTITY";
/* 1389 */       } else if (peek("ENTITIES")) {
/* 1390 */         typeName = "ENTITIES";
/* 1391 */       } else if (peek("NMTOKENS")) {
/* 1392 */         typeName = "NMTOKENS";
/* 1393 */       } else if (peek("NMTOKEN")) {
/* 1394 */         typeName = "NMTOKEN";
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1399 */       else if (peek("NOTATION")) {
/* 1400 */         typeName = "NOTATION";
/* 1401 */         whitespace("F-002");
/* 1402 */         nextChar('(', "F-029", null);
/* 1403 */         maybeWhitespace();
/*      */         
/* 1405 */         values = new Vector();
/*      */         do {
/*      */           String name;
/* 1408 */           if ((name = maybeGetName()) == null) {
/* 1409 */             fatal("P-068");
/*      */           }
/* 1411 */           if (this.notations.get(name) == null)
/* 1412 */             this.notations.put(name, name); 
/* 1413 */           values.addElement(name);
/* 1414 */           maybeWhitespace();
/* 1415 */           if (!peek("|"))
/* 1416 */             continue;  maybeWhitespace();
/* 1417 */         } while (!peek(")"));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1423 */       else if (peek("(")) {
/*      */         
/* 1425 */         typeName = "ENUMERATION";
/*      */         
/* 1427 */         maybeWhitespace();
/*      */ 
/*      */         
/* 1430 */         values = new Vector();
/*      */         do {
/* 1432 */           String name = getNmtoken();
/*      */           
/* 1434 */           values.addElement(name);
/* 1435 */           maybeWhitespace();
/* 1436 */           if (!peek("|"))
/* 1437 */             continue;  maybeWhitespace();
/* 1438 */         } while (!peek(")"));
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1443 */         fatal("P-045", new Object[] { attName, new Character(getc()) });
/*      */         
/* 1445 */         typeName = null;
/*      */       } 
/*      */ 
/*      */       
/* 1449 */       String defaultValue = null;
/*      */ 
/*      */ 
/*      */       
/* 1453 */       whitespace("F-003");
/* 1454 */       if (peek("#REQUIRED")) {
/* 1455 */         attributeUse = 3;
/*      */       }
/* 1457 */       else if (peek("#FIXED")) {
/*      */         
/* 1459 */         if (typeName == "ID") {
/* 1460 */           error("V-017", new Object[] { attName });
/*      */         }
/* 1462 */         attributeUse = 2;
/* 1463 */         whitespace("F-004");
/* 1464 */         parseLiteral(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1470 */         if (typeName == "CDATA") {
/* 1471 */           defaultValue = normalize(false);
/*      */         } else {
/* 1473 */           defaultValue = this.strTmp.toString();
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1478 */       else if (!peek("#IMPLIED")) {
/* 1479 */         attributeUse = 1;
/*      */ 
/*      */         
/* 1482 */         if (typeName == "ID")
/* 1483 */           error("V-018", new Object[] { attName }); 
/* 1484 */         parseLiteral(false);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1489 */         if (typeName == "CDATA") {
/* 1490 */           defaultValue = normalize(false);
/*      */         } else {
/* 1492 */           defaultValue = this.strTmp.toString();
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1499 */         attributeUse = 0;
/*      */       } 
/*      */       
/* 1502 */       if ("xml:lang".equals(attName) && defaultValue != null && !isXmlLang(defaultValue))
/*      */       {
/*      */         
/* 1505 */         error("P-033", new Object[] { defaultValue });
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1513 */       String[] v = (values != null) ? values.<String>toArray(new String[0]) : null;
/* 1514 */       this.dtdHandler.attributeDecl(elementName, attName, typeName, v, attributeUse, defaultValue);
/* 1515 */       maybeWhitespace();
/*      */     } 
/* 1517 */     if (start != this.in)
/* 1518 */       error("V-013", null); 
/* 1519 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String normalize(boolean invalidIfNeeded) {
/* 1530 */     String s = this.strTmp.toString();
/* 1531 */     String s2 = s.trim();
/* 1532 */     boolean didStrip = false;
/*      */     
/* 1534 */     if (s != s2) {
/* 1535 */       s = s2;
/* 1536 */       s2 = null;
/* 1537 */       didStrip = true;
/*      */     } 
/* 1539 */     this.strTmp = new StringBuffer();
/* 1540 */     for (int i = 0; i < s.length(); i++) {
/* 1541 */       char c = s.charAt(i);
/* 1542 */       if (!XmlChars.isSpace(c)) {
/* 1543 */         this.strTmp.append(c);
/*      */       } else {
/*      */         
/* 1546 */         this.strTmp.append(' ');
/* 1547 */         while (++i < s.length() && XmlChars.isSpace(s.charAt(i)))
/* 1548 */           didStrip = true; 
/* 1549 */         i--;
/*      */       } 
/* 1551 */     }  if (didStrip) {
/* 1552 */       return this.strTmp.toString();
/*      */     }
/* 1554 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeConditionalSect() throws IOException, SAXException {
/* 1562 */     if (!peek("<![")) {
/* 1563 */       return false;
/*      */     }
/*      */     
/* 1566 */     InputEntity start = this.in;
/*      */     
/* 1568 */     maybeWhitespace();
/*      */     String keyword;
/* 1570 */     if ((keyword = maybeGetName()) == null)
/* 1571 */       fatal("P-046"); 
/* 1572 */     maybeWhitespace();
/* 1573 */     nextChar('[', "F-030", null);
/*      */ 
/*      */ 
/*      */     
/* 1577 */     if ("INCLUDE".equals(keyword)) {
/*      */       while (true) {
/* 1579 */         if (this.in.isEOF() && this.in != start) {
/* 1580 */           this.in = this.in.pop(); continue;
/* 1581 */         }  if (this.in.isEOF()) {
/* 1582 */           error("V-020", null);
/*      */         }
/* 1584 */         if (peek("]]>")) {
/*      */           break;
/*      */         }
/* 1587 */         this.doLexicalPE = false;
/* 1588 */         if (maybeWhitespace())
/*      */           continue; 
/* 1590 */         if (maybePEReference())
/*      */           continue; 
/* 1592 */         this.doLexicalPE = true;
/* 1593 */         if (maybeMarkupDecl() || maybeConditionalSect()) {
/*      */           continue;
/*      */         }
/* 1596 */         fatal("P-047");
/*      */ 
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 1604 */     else if ("IGNORE".equals(keyword)) {
/* 1605 */       int nestlevel = 1;
/*      */       
/* 1607 */       this.doLexicalPE = false;
/* 1608 */       while (nestlevel > 0) {
/* 1609 */         char c = getc();
/* 1610 */         if (c == '<') {
/* 1611 */           if (peek("!["))
/* 1612 */             nestlevel++;  continue;
/* 1613 */         }  if (c == ']' && 
/* 1614 */           peek("]>")) {
/* 1615 */           nestlevel--;
/*      */         }
/*      */       } 
/*      */     } else {
/*      */       
/* 1620 */       fatal("P-048", new Object[] { keyword });
/* 1621 */     }  return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int parseCharNumber() throws IOException, SAXException {
/* 1634 */     int retval = 0;
/*      */ 
/*      */     
/* 1637 */     if (getc() != 'x') {
/* 1638 */       ungetc();
/*      */       while (true) {
/* 1640 */         char c = getc();
/* 1641 */         if (c >= '0' && c <= '9') {
/* 1642 */           retval *= 10;
/* 1643 */           retval += c - 48;
/*      */           continue;
/*      */         } 
/* 1646 */         if (c == ';')
/* 1647 */           return retval; 
/* 1648 */         fatal("P-049");
/*      */       } 
/*      */     } 
/*      */     while (true) {
/* 1652 */       char c = getc();
/* 1653 */       if (c >= '0' && c <= '9') {
/* 1654 */         retval <<= 4;
/* 1655 */         retval += c - 48;
/*      */         continue;
/*      */       } 
/* 1658 */       if (c >= 'a' && c <= 'f') {
/* 1659 */         retval <<= 4;
/* 1660 */         retval += 10 + c - 97;
/*      */         continue;
/*      */       } 
/* 1663 */       if (c >= 'A' && c <= 'F') {
/* 1664 */         retval <<= 4;
/* 1665 */         retval += 10 + c - 65;
/*      */         continue;
/*      */       } 
/* 1668 */       if (c == ';')
/* 1669 */         return retval; 
/* 1670 */       fatal("P-050");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int surrogatesToCharTmp(int ucs4) throws SAXException {
/* 1679 */     if (ucs4 <= 65535) {
/* 1680 */       if (XmlChars.isChar(ucs4)) {
/* 1681 */         this.charTmp[0] = (char)ucs4;
/* 1682 */         return 1;
/*      */       } 
/* 1684 */     } else if (ucs4 <= 1114111) {
/*      */       
/* 1686 */       ucs4 -= 65536;
/* 1687 */       this.charTmp[0] = (char)(0xD800 | ucs4 >> 10 & 0x3FF);
/* 1688 */       this.charTmp[1] = (char)(0xDC00 | ucs4 & 0x3FF);
/* 1689 */       return 2;
/*      */     } 
/* 1691 */     fatal("P-051", new Object[] { Integer.toHexString(ucs4) });
/*      */     
/* 1693 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybePEReference() throws IOException, SAXException {
/* 1704 */     if (!this.in.peekc('%')) {
/* 1705 */       return false;
/*      */     }
/* 1707 */     String name = maybeGetName();
/*      */ 
/*      */     
/* 1710 */     if (name == null)
/* 1711 */       fatal("P-011"); 
/* 1712 */     nextChar(';', "F-021", name);
/* 1713 */     Object entity = this.params.get(name);
/*      */     
/* 1715 */     if (entity instanceof InternalEntity) {
/* 1716 */       InternalEntity value = (InternalEntity)entity;
/* 1717 */       pushReader(value.buf, name, false);
/*      */     }
/* 1719 */     else if (entity instanceof ExternalEntity) {
/* 1720 */       pushReader((ExternalEntity)entity);
/* 1721 */       externalParameterEntity((ExternalEntity)entity);
/*      */     }
/* 1723 */     else if (entity == null) {
/* 1724 */       error("V-022", new Object[] { name });
/*      */     } 
/* 1726 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeEntityDecl() throws IOException, SAXException {
/*      */     SimpleHashtable defns;
/* 1738 */     InputEntity start = peekDeclaration("!ENTITY");
/*      */     
/* 1740 */     if (start == null) {
/* 1741 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1755 */     this.doLexicalPE = false;
/* 1756 */     whitespace("F-005");
/* 1757 */     if (this.in.peekc('%')) {
/* 1758 */       whitespace("F-006");
/* 1759 */       defns = this.params;
/*      */     } else {
/* 1761 */       defns = this.entities;
/*      */     } 
/* 1763 */     ungetc();
/* 1764 */     this.doLexicalPE = true;
/* 1765 */     String entityName = getMarkupDeclname("F-017", false);
/* 1766 */     whitespace("F-007");
/* 1767 */     ExternalEntity externalId = maybeExternalID();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1776 */     boolean doStore = (defns.get(entityName) == null);
/* 1777 */     if (!doStore && defns == this.entities) {
/* 1778 */       warning("P-054", new Object[] { entityName });
/*      */     }
/*      */     
/* 1781 */     if (externalId == null) {
/*      */ 
/*      */ 
/*      */       
/* 1785 */       this.doLexicalPE = false;
/* 1786 */       parseLiteral(true);
/* 1787 */       this.doLexicalPE = true;
/* 1788 */       if (doStore) {
/* 1789 */         char[] value = new char[this.strTmp.length()];
/* 1790 */         if (value.length != 0)
/* 1791 */           this.strTmp.getChars(0, value.length, value, 0); 
/* 1792 */         InternalEntity entity = new InternalEntity(entityName, value);
/* 1793 */         entity.isPE = (defns == this.params);
/* 1794 */         entity.isFromInternalSubset = false;
/* 1795 */         defns.put(entityName, entity);
/* 1796 */         if (defns == this.entities) {
/* 1797 */           this.dtdHandler.internalGeneralEntityDecl(entityName, new String(value));
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1804 */       if (defns == this.entities && maybeWhitespace() && peek("NDATA")) {
/*      */         
/* 1806 */         externalId.notation = getMarkupDeclname("F-018", false);
/*      */ 
/*      */ 
/*      */         
/* 1810 */         if (this.notations.get(externalId.notation) == null)
/* 1811 */           this.notations.put(externalId.notation, Boolean.TRUE); 
/*      */       } 
/* 1813 */       externalId.name = entityName;
/* 1814 */       externalId.isPE = (defns == this.params);
/* 1815 */       externalId.isFromInternalSubset = false;
/* 1816 */       if (doStore) {
/* 1817 */         defns.put(entityName, externalId);
/* 1818 */         if (externalId.notation != null) {
/* 1819 */           this.dtdHandler.unparsedEntityDecl(entityName, externalId.publicId, externalId.systemId, externalId.notation);
/*      */         
/*      */         }
/* 1822 */         else if (defns == this.entities) {
/* 1823 */           this.dtdHandler.externalGeneralEntityDecl(entityName, externalId.publicId, externalId.systemId);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1827 */     maybeWhitespace();
/* 1828 */     nextChar('>', "F-031", entityName);
/* 1829 */     if (start != this.in)
/* 1830 */       error("V-013", null); 
/* 1831 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ExternalEntity maybeExternalID() throws IOException, SAXException {
/* 1839 */     String temp = null;
/*      */ 
/*      */     
/* 1842 */     if (peek("PUBLIC")) {
/* 1843 */       whitespace("F-009");
/* 1844 */       temp = parsePublicId();
/* 1845 */     } else if (!peek("SYSTEM")) {
/* 1846 */       return null;
/*      */     } 
/* 1848 */     ExternalEntity retval = new ExternalEntity(this.in);
/* 1849 */     retval.publicId = temp;
/* 1850 */     whitespace("F-008");
/* 1851 */     retval.systemId = parseSystemId();
/* 1852 */     return retval;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String parseSystemId() throws IOException, SAXException {
/* 1858 */     String uri = getQuotedString("F-034", null);
/* 1859 */     int temp = uri.indexOf(':');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1868 */     if (temp == -1 || uri.indexOf('/') < temp) {
/*      */ 
/*      */       
/* 1871 */       String baseURI = this.in.getSystemId();
/* 1872 */       if (baseURI == null)
/* 1873 */         fatal("P-055", new Object[] { uri }); 
/* 1874 */       if (uri.length() == 0)
/* 1875 */         uri = "."; 
/* 1876 */       baseURI = baseURI.substring(0, baseURI.lastIndexOf('/') + 1);
/* 1877 */       if (uri.charAt(0) != '/') {
/* 1878 */         uri = baseURI + uri;
/*      */       }
/*      */       else {
/*      */         
/* 1882 */         throw new InternalError();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1889 */     if (uri.indexOf('#') != -1)
/* 1890 */       error("P-056", new Object[] { uri }); 
/* 1891 */     return uri;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void maybeTextDecl() throws IOException, SAXException {
/* 1898 */     if (peek("<?xml")) {
/* 1899 */       readVersion(false, "1.0");
/* 1900 */       readEncoding(true);
/* 1901 */       maybeWhitespace();
/* 1902 */       if (!peek("?>")) {
/* 1903 */         fatal("P-057");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void externalParameterEntity(ExternalEntity next) throws IOException, SAXException {
/* 1928 */     InputEntity pe = this.in;
/* 1929 */     maybeTextDecl();
/* 1930 */     while (!pe.isEOF()) {
/*      */       
/* 1932 */       if (this.in.isEOF()) {
/* 1933 */         this.in = this.in.pop();
/*      */         continue;
/*      */       } 
/* 1936 */       this.doLexicalPE = false;
/* 1937 */       if (maybeWhitespace())
/*      */         continue; 
/* 1939 */       if (maybePEReference())
/*      */         continue; 
/* 1941 */       this.doLexicalPE = true;
/* 1942 */       if (maybeMarkupDecl() || maybeConditionalSect());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1947 */     if (!pe.isEOF()) {
/* 1948 */       fatal("P-059", new Object[] { this.in.getName() });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void readEncoding(boolean must) throws IOException, SAXException {
/* 1955 */     String name = maybeReadAttribute("encoding", must);
/*      */     
/* 1957 */     if (name == null)
/*      */       return; 
/* 1959 */     for (int i = 0; i < name.length(); i++) {
/* 1960 */       char c = name.charAt(i);
/* 1961 */       if ((c < 'A' || c > 'Z') && (c < 'a' || c > 'z'))
/*      */       {
/*      */         
/* 1964 */         if (i == 0 || ((c < '0' || c > '9') && c != '-' && c != '_' && c != '.'))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1971 */           fatal("P-060", new Object[] { new Character(c) });
/*      */         }
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1983 */     String currentEncoding = this.in.getEncoding();
/*      */     
/* 1985 */     if (currentEncoding != null && !name.equalsIgnoreCase(currentEncoding))
/*      */     {
/* 1987 */       warning("P-061", new Object[] { name, currentEncoding });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean maybeNotationDecl() throws IOException, SAXException {
/* 1996 */     InputEntity start = peekDeclaration("!NOTATION");
/*      */     
/* 1998 */     if (start == null) {
/* 1999 */       return false;
/*      */     }
/* 2001 */     String name = getMarkupDeclname("F-019", false);
/* 2002 */     ExternalEntity entity = new ExternalEntity(this.in);
/*      */     
/* 2004 */     whitespace("F-011");
/* 2005 */     if (peek("PUBLIC")) {
/* 2006 */       whitespace("F-009");
/* 2007 */       entity.publicId = parsePublicId();
/* 2008 */       if (maybeWhitespace())
/* 2009 */         if (!peek(">")) {
/* 2010 */           entity.systemId = parseSystemId();
/*      */         } else {
/* 2012 */           ungetc();
/*      */         }  
/* 2014 */     } else if (peek("SYSTEM")) {
/* 2015 */       whitespace("F-008");
/* 2016 */       entity.systemId = parseSystemId();
/*      */     } else {
/* 2018 */       fatal("P-062");
/* 2019 */     }  maybeWhitespace();
/* 2020 */     nextChar('>', "F-032", name);
/* 2021 */     if (start != this.in)
/* 2022 */       error("V-013", null); 
/* 2023 */     if (entity.systemId != null && entity.systemId.indexOf('#') != -1) {
/* 2024 */       error("P-056", new Object[] { entity.systemId });
/*      */     }
/* 2026 */     Object value = this.notations.get(name);
/* 2027 */     if (value != null && value instanceof ExternalEntity) {
/* 2028 */       warning("P-063", new Object[] { name });
/*      */     } else {
/*      */       
/* 2031 */       this.notations.put(name, entity);
/* 2032 */       this.dtdHandler.notationDecl(name, entity.publicId, entity.systemId);
/*      */     } 
/*      */     
/* 2035 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char getc() throws IOException, SAXException {
/* 2047 */     if (!this.doLexicalPE) {
/* 2048 */       char c1 = this.in.getc();
/* 2049 */       return c1;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2070 */     while (this.in.isEOF()) {
/* 2071 */       if (this.in.isInternal() || (this.doLexicalPE && !this.in.isDocument())) {
/* 2072 */         this.in = this.in.pop(); continue;
/*      */       } 
/* 2074 */       fatal("P-064", new Object[] { this.in.getName() });
/*      */     } 
/*      */     char c;
/* 2077 */     if ((c = this.in.getc()) == '%' && this.doLexicalPE) {
/*      */       
/* 2079 */       String name = maybeGetName();
/*      */ 
/*      */       
/* 2082 */       if (name == null)
/* 2083 */         fatal("P-011"); 
/* 2084 */       nextChar(';', "F-021", name);
/* 2085 */       Object entity = this.params.get(name);
/*      */ 
/*      */ 
/*      */       
/* 2089 */       pushReader(" ".toCharArray(), null, false);
/* 2090 */       if (entity instanceof InternalEntity) {
/* 2091 */         pushReader(((InternalEntity)entity).buf, name, false);
/* 2092 */       } else if (entity instanceof ExternalEntity) {
/*      */ 
/*      */         
/* 2095 */         pushReader((ExternalEntity)entity);
/* 2096 */       } else if (entity == null) {
/*      */         
/* 2098 */         fatal("V-022");
/*      */       } else {
/* 2100 */         throw new InternalError();
/* 2101 */       }  pushReader(" ".toCharArray(), null, false);
/* 2102 */       return this.in.getc();
/*      */     } 
/* 2104 */     return c;
/*      */   }
/*      */ 
/*      */   
/*      */   private void ungetc() {
/* 2109 */     this.in.ungetc();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean peek(String s) throws IOException, SAXException {
/* 2115 */     return this.in.peek(s, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputEntity peekDeclaration(String s) throws IOException, SAXException {
/* 2126 */     if (!this.in.peekc('<'))
/* 2127 */       return null; 
/* 2128 */     InputEntity start = this.in;
/* 2129 */     if (this.in.peek(s, null))
/* 2130 */       return start; 
/* 2131 */     this.in.ungetc();
/* 2132 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void nextChar(char c, String location, String near) throws IOException, SAXException {
/* 2138 */     while (this.in.isEOF() && !this.in.isDocument())
/* 2139 */       this.in = this.in.pop(); 
/* 2140 */     if (!this.in.peekc(c)) {
/* 2141 */       fatal("P-008", new Object[] { new Character(c), messages.getMessage(this.locale, location), (near == null) ? "" : ('"' + near + '"') });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void pushReader(char[] buf, String name, boolean isGeneral) throws SAXException {
/* 2151 */     InputEntity r = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/* 2152 */     r.init(buf, name, this.in, !isGeneral);
/* 2153 */     this.in = r;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean pushReader(ExternalEntity next) throws IOException, SAXException {
/*      */     InputSource s;
/* 2159 */     InputEntity r = InputEntity.getInputEntity(this.dtdHandler, this.locale);
/*      */     
/*      */     try {
/* 2162 */       s = next.getInputSource(this.resolver);
/* 2163 */     } catch (IOException e) {
/* 2164 */       String msg = "unable to open the external entity from :" + next.systemId;
/*      */       
/* 2166 */       if (next.publicId != null) {
/* 2167 */         msg = msg + " (public id:" + next.publicId + ")";
/*      */       }
/* 2169 */       SAXParseException spe = new SAXParseException(msg, getPublicId(), getSystemId(), getLineNumber(), getColumnNumber(), e);
/*      */       
/* 2171 */       this.dtdHandler.fatalError(spe);
/* 2172 */       throw e;
/*      */     } 
/*      */     
/* 2175 */     r.init(s, next.name, this.in, next.isPE);
/* 2176 */     this.in = r;
/* 2177 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getPublicId() {
/* 2182 */     return (this.in == null) ? null : this.in.getPublicId();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSystemId() {
/* 2187 */     return (this.in == null) ? null : this.in.getSystemId();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getLineNumber() {
/* 2192 */     return (this.in == null) ? -1 : this.in.getLineNumber();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getColumnNumber() {
/* 2197 */     return (this.in == null) ? -1 : this.in.getColumnNumber();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void warning(String messageId, Object[] parameters) throws SAXException {
/* 2205 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/*      */     
/* 2208 */     this.dtdHandler.warning(e);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void error(String messageId, Object[] parameters) throws SAXException {
/* 2214 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/*      */     
/* 2217 */     this.dtdHandler.error(e);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fatal(String messageId) throws SAXException {
/* 2222 */     fatal(messageId, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void fatal(String messageId, Object[] parameters) throws SAXException {
/* 2228 */     SAXParseException e = new SAXParseException(messages.getMessage(this.locale, messageId, parameters), getPublicId(), getSystemId(), getLineNumber(), getColumnNumber());
/*      */ 
/*      */     
/* 2231 */     this.dtdHandler.fatalError(e);
/*      */     
/* 2233 */     throw e;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class NameCache
/*      */   {
/* 2252 */     DTDParser.NameCacheEntry[] hashtable = new DTDParser.NameCacheEntry[541];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String lookup(char[] value, int len) {
/* 2259 */       return (lookupEntry(value, len)).name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DTDParser.NameCacheEntry lookupEntry(char[] value, int len) {
/* 2269 */       int index = 0;
/*      */ 
/*      */ 
/*      */       
/* 2273 */       for (int i = 0; i < len; i++)
/* 2274 */         index = index * 31 + value[i]; 
/* 2275 */       index &= Integer.MAX_VALUE;
/* 2276 */       index %= this.hashtable.length;
/*      */ 
/*      */       
/* 2279 */       DTDParser.NameCacheEntry entry = this.hashtable[index];
/* 2280 */       for (; entry != null; 
/* 2281 */         entry = entry.next) {
/* 2282 */         if (entry.matches(value, len)) {
/* 2283 */           return entry;
/*      */         }
/*      */       } 
/*      */       
/* 2287 */       entry = new DTDParser.NameCacheEntry();
/* 2288 */       entry.chars = new char[len];
/* 2289 */       System.arraycopy(value, 0, entry.chars, 0, len);
/* 2290 */       entry.name = new String(entry.chars);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2296 */       entry.name = entry.name.intern();
/* 2297 */       entry.next = this.hashtable[index];
/* 2298 */       this.hashtable[index] = entry;
/* 2299 */       return entry;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class NameCacheEntry
/*      */   {
/*      */     String name;
/*      */     char[] chars;
/*      */     NameCacheEntry next;
/*      */     
/*      */     boolean matches(char[] value, int len) {
/* 2311 */       if (this.chars.length != len)
/* 2312 */         return false; 
/* 2313 */       for (int i = 0; i < len; i++) {
/* 2314 */         if (value[i] != this.chars[i])
/* 2315 */           return false; 
/* 2316 */       }  return true;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2323 */   static final Catalog messages = new Catalog();
/*      */   static Class class$com$sun$xml$dtdparser$DTDParser;
/*      */   
/*      */   static Class class$(String x0) {
/*      */     
/* 2328 */     try { return Class.forName(x0); } catch (ClassNotFoundException x1) { throw new NoClassDefFoundError(x1.getMessage()); }  } static final class Catalog extends MessageCatalog { Catalog() { super((DTDParser.class$com$sun$xml$dtdparser$DTDParser == null) ? (DTDParser.class$com$sun$xml$dtdparser$DTDParser = DTDParser.class$("com.sun.xml.dtdparser.DTDParser")) : DTDParser.class$com$sun$xml$dtdparser$DTDParser); }
/*      */      }
/*      */ 
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\dtdparser\DTDParser.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */
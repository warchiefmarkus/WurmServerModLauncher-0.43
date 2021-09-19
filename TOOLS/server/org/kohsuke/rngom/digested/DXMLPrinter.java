/*     */ package org.kohsuke.rngom.digested;
/*     */ 
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLOutputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.util.CheckingSchemaBuilder;
/*     */ import org.kohsuke.rngom.nc.NameClass;
/*     */ import org.kohsuke.rngom.nc.NameClassVisitor;
/*     */ import org.kohsuke.rngom.nc.SimpleNameClass;
/*     */ import org.kohsuke.rngom.parse.compact.CompactParseable;
/*     */ import org.kohsuke.rngom.parse.xml.SAXParseable;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DXMLPrinter
/*     */ {
/*     */   protected XMLStreamWriter out;
/*  37 */   protected String indentStep = "\t";
/*  38 */   protected String newLine = System.getProperty("line.separator");
/*     */   
/*     */   protected int indent;
/*     */   
/*     */   protected boolean afterEnd = false;
/*     */   
/*     */   protected DXMLPrinterVisitor visitor;
/*     */   protected NameClassXMLPrinterVisitor ncVisitor;
/*     */   protected DOMPrinter domPrinter;
/*     */   
/*     */   public DXMLPrinter(XMLStreamWriter out) {
/*  49 */     this.out = out;
/*  50 */     this.visitor = new DXMLPrinterVisitor();
/*  51 */     this.ncVisitor = new NameClassXMLPrinterVisitor();
/*  52 */     this.domPrinter = new DOMPrinter(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printDocument(DGrammarPattern grammar) throws XMLStreamException {
/*     */     try {
/*  63 */       this.visitor.startDocument();
/*  64 */       this.visitor.on(grammar);
/*  65 */       this.visitor.endDocument();
/*  66 */     } catch (XMLWriterException e) {
/*  67 */       throw (XMLStreamException)e.getCause();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(DPattern pattern) throws XMLStreamException {
/*     */     try {
/*  78 */       pattern.accept(this.visitor);
/*  79 */     } catch (XMLWriterException e) {
/*  80 */       throw (XMLStreamException)e.getCause();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(NameClass nc) throws XMLStreamException {
/*     */     try {
/*  91 */       nc.accept(this.ncVisitor);
/*  92 */     } catch (XMLWriterException e) {
/*  93 */       throw (XMLStreamException)e.getCause();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void print(Node node) throws XMLStreamException {
/*  98 */     this.domPrinter.print(node);
/*     */   }
/*     */   
/*     */   protected class XMLWriterException extends RuntimeException {
/*     */     protected XMLWriterException(Throwable cause) {
/* 103 */       super(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class XMLWriter {
/*     */     protected void newLine() {
/*     */       try {
/* 110 */         DXMLPrinter.this.out.writeCharacters(DXMLPrinter.this.newLine);
/* 111 */       } catch (XMLStreamException e) {
/* 112 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void indent() {
/*     */       try {
/* 118 */         for (int i = 0; i < DXMLPrinter.this.indent; i++) {
/* 119 */           DXMLPrinter.this.out.writeCharacters(DXMLPrinter.this.indentStep);
/*     */         }
/* 121 */       } catch (XMLStreamException e) {
/* 122 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void startDocument() {
/*     */       try {
/* 128 */         DXMLPrinter.this.out.writeStartDocument();
/* 129 */       } catch (XMLStreamException e) {
/* 130 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endDocument() {
/*     */       try {
/* 136 */         DXMLPrinter.this.out.writeEndDocument();
/* 137 */       } catch (XMLStreamException e) {
/* 138 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public final void start(String element) {
/*     */       try {
/* 144 */         newLine();
/* 145 */         indent();
/* 146 */         DXMLPrinter.this.out.writeStartElement(element);
/* 147 */         DXMLPrinter.this.indent++;
/* 148 */         DXMLPrinter.this.afterEnd = false;
/* 149 */       } catch (XMLStreamException e) {
/* 150 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void end() {
/*     */       try {
/* 156 */         DXMLPrinter.this.indent--;
/* 157 */         if (DXMLPrinter.this.afterEnd) {
/* 158 */           newLine();
/* 159 */           indent();
/*     */         } 
/* 161 */         DXMLPrinter.this.out.writeEndElement();
/* 162 */         DXMLPrinter.this.afterEnd = true;
/* 163 */       } catch (XMLStreamException e) {
/* 164 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void attr(String prefix, String ns, String name, String value) {
/*     */       try {
/* 170 */         DXMLPrinter.this.out.writeAttribute(prefix, ns, name, value);
/* 171 */       } catch (XMLStreamException e) {
/* 172 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void attr(String name, String value) {
/*     */       try {
/* 178 */         DXMLPrinter.this.out.writeAttribute(name, value);
/* 179 */       } catch (XMLStreamException e) {
/* 180 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void ns(String prefix, String uri) {
/*     */       try {
/* 186 */         DXMLPrinter.this.out.writeNamespace(prefix, uri);
/* 187 */       } catch (XMLStreamException e) {
/* 188 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void body(String text) {
/*     */       try {
/* 194 */         DXMLPrinter.this.out.writeCharacters(text);
/* 195 */         DXMLPrinter.this.afterEnd = false;
/* 196 */       } catch (XMLStreamException e) {
/* 197 */         throw new DXMLPrinter.XMLWriterException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected class DXMLPrinterVisitor extends XMLWriter implements DPatternVisitor<Void> {
/*     */     protected void on(DPattern p) {
/* 204 */       p.accept(this);
/*     */     }
/*     */     
/*     */     protected void unwrapGroup(DPattern p) {
/* 208 */       if (p instanceof DGroupPattern && p.getAnnotation() == DAnnotation.EMPTY) {
/* 209 */         for (DPattern d : p) {
/* 210 */           on(d);
/*     */         }
/*     */       } else {
/* 213 */         on(p);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void unwrapChoice(DPattern p) {
/* 218 */       if (p instanceof DChoicePattern && p.getAnnotation() == DAnnotation.EMPTY) {
/* 219 */         for (DPattern d : p) {
/* 220 */           on(d);
/*     */         }
/*     */       } else {
/* 223 */         on(p);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void on(NameClass nc) {
/* 228 */       if (nc instanceof SimpleNameClass) {
/* 229 */         QName qname = ((SimpleNameClass)nc).name;
/* 230 */         String name = qname.getLocalPart();
/* 231 */         if (!qname.getPrefix().equals("")) name = qname.getPrefix() + ":"; 
/* 232 */         attr("name", name);
/*     */       } else {
/* 234 */         nc.accept(DXMLPrinter.this.ncVisitor);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void on(DAnnotation ann) {
/* 239 */       if (ann == DAnnotation.EMPTY)
/* 240 */         return;  for (DAnnotation.Attribute attr : ann.getAttributes().values()) {
/* 241 */         attr(attr.getPrefix(), attr.getNs(), attr.getLocalName(), attr.getValue());
/*     */       }
/* 243 */       for (Element elem : ann.getChildren()) {
/*     */         try {
/* 245 */           newLine();
/* 246 */           indent();
/* 247 */           DXMLPrinter.this.print(elem);
/*     */         }
/* 249 */         catch (XMLStreamException e) {
/* 250 */           throw new DXMLPrinter.XMLWriterException(e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Void onAttribute(DAttributePattern p) {
/* 256 */       start("attribute");
/* 257 */       on(p.getName());
/* 258 */       on(p.getAnnotation());
/* 259 */       DPattern child = p.getChild();
/*     */       
/* 261 */       if (!(child instanceof DTextPattern)) {
/* 262 */         on(p.getChild());
/*     */       }
/* 264 */       end();
/* 265 */       return null;
/*     */     }
/*     */     
/*     */     public Void onChoice(DChoicePattern p) {
/* 269 */       start("choice");
/* 270 */       on(p.getAnnotation());
/* 271 */       for (DPattern d : p) {
/* 272 */         on(d);
/*     */       }
/* 274 */       end();
/* 275 */       return null;
/*     */     }
/*     */     
/*     */     public Void onData(DDataPattern p) {
/* 279 */       List<DDataPattern.Param> params = p.getParams();
/* 280 */       DPattern except = p.getExcept();
/* 281 */       start("data");
/* 282 */       attr("datatypeLibrary", p.getDatatypeLibrary());
/* 283 */       attr("type", p.getType());
/* 284 */       on(p.getAnnotation());
/* 285 */       for (DDataPattern.Param param : params) {
/* 286 */         start("param");
/* 287 */         attr("ns", param.getNs());
/* 288 */         attr("name", param.getName());
/* 289 */         body(param.getValue());
/* 290 */         end();
/*     */       } 
/* 292 */       if (except != null) {
/* 293 */         start("except");
/* 294 */         unwrapChoice(except);
/* 295 */         end();
/*     */       } 
/* 297 */       end();
/* 298 */       return null;
/*     */     }
/*     */     
/*     */     public Void onElement(DElementPattern p) {
/* 302 */       start("element");
/* 303 */       on(p.getName());
/* 304 */       on(p.getAnnotation());
/* 305 */       unwrapGroup(p.getChild());
/* 306 */       end();
/* 307 */       return null;
/*     */     }
/*     */     
/*     */     public Void onEmpty(DEmptyPattern p) {
/* 311 */       start("empty");
/* 312 */       on(p.getAnnotation());
/* 313 */       end();
/* 314 */       return null;
/*     */     }
/*     */     
/*     */     public Void onGrammar(DGrammarPattern p) {
/* 318 */       start("grammar");
/* 319 */       ns(null, "http://relaxng.org/ns/structure/1.0");
/* 320 */       on(p.getAnnotation());
/* 321 */       start("start");
/* 322 */       on(p.getStart());
/* 323 */       end();
/* 324 */       for (DDefine d : p) {
/* 325 */         start("define");
/* 326 */         attr("name", d.getName());
/* 327 */         on(d.getAnnotation());
/* 328 */         unwrapGroup(d.getPattern());
/* 329 */         end();
/*     */       } 
/* 331 */       end();
/* 332 */       return null;
/*     */     }
/*     */     
/*     */     public Void onGroup(DGroupPattern p) {
/* 336 */       start("group");
/* 337 */       on(p.getAnnotation());
/* 338 */       for (DPattern d : p) {
/* 339 */         on(d);
/*     */       }
/* 341 */       end();
/* 342 */       return null;
/*     */     }
/*     */     
/*     */     public Void onInterleave(DInterleavePattern p) {
/* 346 */       start("interleave");
/* 347 */       on(p.getAnnotation());
/* 348 */       for (DPattern d : p) {
/* 349 */         on(d);
/*     */       }
/* 351 */       end();
/* 352 */       return null;
/*     */     }
/*     */     
/*     */     public Void onList(DListPattern p) {
/* 356 */       start("list");
/* 357 */       on(p.getAnnotation());
/* 358 */       unwrapGroup(p.getChild());
/* 359 */       end();
/* 360 */       return null;
/*     */     }
/*     */     
/*     */     public Void onMixed(DMixedPattern p) {
/* 364 */       start("mixed");
/* 365 */       on(p.getAnnotation());
/* 366 */       unwrapGroup(p.getChild());
/* 367 */       end();
/* 368 */       return null;
/*     */     }
/*     */     
/*     */     public Void onNotAllowed(DNotAllowedPattern p) {
/* 372 */       start("notAllowed");
/* 373 */       on(p.getAnnotation());
/* 374 */       end();
/* 375 */       return null;
/*     */     }
/*     */     
/*     */     public Void onOneOrMore(DOneOrMorePattern p) {
/* 379 */       start("oneOrMore");
/* 380 */       on(p.getAnnotation());
/* 381 */       unwrapGroup(p.getChild());
/* 382 */       end();
/* 383 */       return null;
/*     */     }
/*     */     
/*     */     public Void onOptional(DOptionalPattern p) {
/* 387 */       start("optional");
/* 388 */       on(p.getAnnotation());
/* 389 */       unwrapGroup(p.getChild());
/* 390 */       end();
/* 391 */       return null;
/*     */     }
/*     */     
/*     */     public Void onRef(DRefPattern p) {
/* 395 */       start("ref");
/* 396 */       attr("name", p.getName());
/* 397 */       on(p.getAnnotation());
/* 398 */       end();
/* 399 */       return null;
/*     */     }
/*     */     
/*     */     public Void onText(DTextPattern p) {
/* 403 */       start("text");
/* 404 */       on(p.getAnnotation());
/* 405 */       end();
/* 406 */       return null;
/*     */     }
/*     */     
/*     */     public Void onValue(DValuePattern p) {
/* 410 */       start("value");
/* 411 */       if (!p.getNs().equals("")) attr("ns", p.getNs()); 
/* 412 */       attr("datatypeLibrary", p.getDatatypeLibrary());
/* 413 */       attr("type", p.getType());
/* 414 */       on(p.getAnnotation());
/* 415 */       body(p.getValue());
/* 416 */       end();
/* 417 */       return null;
/*     */     }
/*     */     
/*     */     public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 421 */       start("zeroOrMore");
/* 422 */       on(p.getAnnotation());
/* 423 */       unwrapGroup(p.getChild());
/* 424 */       end();
/* 425 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class NameClassXMLPrinterVisitor
/*     */     extends XMLWriter implements NameClassVisitor<Void> {
/*     */     public Void visitChoice(NameClass nc1, NameClass nc2) {
/* 432 */       start("choice");
/* 433 */       nc1.accept(this);
/* 434 */       nc2.accept(this);
/* 435 */       end();
/* 436 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitNsName(String ns) {
/* 440 */       start("nsName");
/* 441 */       attr("ns", ns);
/* 442 */       end();
/* 443 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitNsNameExcept(String ns, NameClass nc) {
/* 447 */       start("nsName");
/* 448 */       attr("ns", ns);
/* 449 */       start("except");
/* 450 */       nc.accept(this);
/* 451 */       end();
/* 452 */       end();
/* 453 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitAnyName() {
/* 457 */       start("anyName");
/* 458 */       end();
/* 459 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitAnyNameExcept(NameClass nc) {
/* 463 */       start("anyName");
/* 464 */       start("except");
/* 465 */       nc.accept(this);
/* 466 */       end();
/* 467 */       end();
/* 468 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitName(QName name) {
/* 472 */       start("name");
/* 473 */       if (!name.getPrefix().equals("")) {
/* 474 */         body(name.getPrefix() + ":");
/*     */       }
/* 476 */       body(name.getLocalPart());
/* 477 */       end();
/* 478 */       return null;
/*     */     }
/*     */     
/*     */     public Void visitNull() {
/* 482 */       throw new UnsupportedOperationException("visitNull");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*     */     CompactParseable compactParseable;
/* 489 */     ErrorHandler eh = new DefaultHandler() {
/*     */         public void error(SAXParseException e) throws SAXException {
/* 491 */           throw e;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 496 */     if (args[0].endsWith(".rng")) {
/* 497 */       SAXParseable sAXParseable = new SAXParseable(new InputSource(args[0]), eh);
/*     */     } else {
/* 499 */       compactParseable = new CompactParseable(new InputSource(args[0]), eh);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 506 */     CheckingSchemaBuilder checkingSchemaBuilder = new CheckingSchemaBuilder(new DSchemaBuilderImpl(), eh);
/*     */     
/*     */     try {
/* 509 */       DGrammarPattern grammar = (DGrammarPattern)compactParseable.parse((SchemaBuilder)checkingSchemaBuilder);
/* 510 */       OutputStream out = new FileOutputStream(args[1]);
/* 511 */       XMLOutputFactory factory = XMLOutputFactory.newInstance();
/* 512 */       XMLStreamWriter output = factory.createXMLStreamWriter(out);
/* 513 */       DXMLPrinter printer = new DXMLPrinter(output);
/* 514 */       printer.printDocument(grammar);
/* 515 */       output.close();
/* 516 */       out.close();
/* 517 */     } catch (BuildException e) {
/* 518 */       if (e.getCause() instanceof SAXParseException) {
/* 519 */         SAXParseException se = (SAXParseException)e.getCause();
/* 520 */         System.out.println("(" + se.getLineNumber() + "," + se.getColumnNumber() + "): " + se.getMessage());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 531 */       if (e.getCause() instanceof SAXException) {
/* 532 */         SAXException se = (SAXException)e.getCause();
/* 533 */         if (se.getException() != null)
/* 534 */           se.getException().printStackTrace(); 
/*     */       } 
/* 536 */       throw e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DXMLPrinter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
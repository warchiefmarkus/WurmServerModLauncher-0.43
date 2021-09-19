/*     */ package org.kohsuke.rngom.binary;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import org.kohsuke.rngom.ast.builder.Annotations;
/*     */ import org.kohsuke.rngom.ast.builder.BuildException;
/*     */ import org.kohsuke.rngom.ast.builder.CommentList;
/*     */ import org.kohsuke.rngom.ast.builder.DataPatternBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Div;
/*     */ import org.kohsuke.rngom.ast.builder.ElementAnnotationBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Grammar;
/*     */ import org.kohsuke.rngom.ast.builder.GrammarSection;
/*     */ import org.kohsuke.rngom.ast.builder.Include;
/*     */ import org.kohsuke.rngom.ast.builder.IncludedGrammar;
/*     */ import org.kohsuke.rngom.ast.builder.NameClassBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.SchemaBuilder;
/*     */ import org.kohsuke.rngom.ast.builder.Scope;
/*     */ import org.kohsuke.rngom.ast.om.Location;
/*     */ import org.kohsuke.rngom.ast.om.ParsedElementAnnotation;
/*     */ import org.kohsuke.rngom.ast.om.ParsedNameClass;
/*     */ import org.kohsuke.rngom.ast.om.ParsedPattern;
/*     */ import org.kohsuke.rngom.ast.util.LocatorImpl;
/*     */ import org.kohsuke.rngom.dt.CascadingDatatypeLibraryFactory;
/*     */ import org.kohsuke.rngom.dt.builtin.BuiltinDatatypeLibraryFactory;
/*     */ import org.kohsuke.rngom.nc.NameClass;
/*     */ import org.kohsuke.rngom.nc.NameClassBuilderImpl;
/*     */ import org.kohsuke.rngom.parse.Context;
/*     */ import org.kohsuke.rngom.parse.IllegalSchemaException;
/*     */ import org.kohsuke.rngom.parse.Parseable;
/*     */ import org.kohsuke.rngom.util.Localizer;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ import org.relaxng.datatype.DatatypeBuilder;
/*     */ import org.relaxng.datatype.DatatypeException;
/*     */ import org.relaxng.datatype.DatatypeLibrary;
/*     */ import org.relaxng.datatype.DatatypeLibraryFactory;
/*     */ import org.relaxng.datatype.ValidationContext;
/*     */ import org.relaxng.datatype.helpers.DatatypeLibraryLoader;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public class SchemaBuilderImpl
/*     */   implements SchemaBuilder, ElementAnnotationBuilder, CommentList {
/*     */   private final SchemaBuilderImpl parent;
/*     */   private boolean hadError = false;
/*     */   private final SchemaPatternBuilder pb;
/*     */   private final DatatypeLibraryFactory datatypeLibraryFactory;
/*     */   private final String inheritNs;
/*     */   private final ErrorHandler eh;
/*     */   private final OpenIncludes openIncludes;
/*  53 */   private final NameClassBuilder ncb = (NameClassBuilder)new NameClassBuilderImpl();
/*  54 */   static final Localizer localizer = new Localizer(SchemaBuilderImpl.class);
/*     */   
/*     */   static class OpenIncludes {
/*     */     final String uri;
/*     */     final OpenIncludes parent;
/*     */     
/*     */     OpenIncludes(String uri, OpenIncludes parent) {
/*  61 */       this.uri = uri;
/*  62 */       this.parent = parent;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern expandPattern(ParsedPattern _pattern) throws BuildException, IllegalSchemaException {
/*  68 */     Pattern pattern = (Pattern)_pattern;
/*  69 */     if (!this.hadError)
/*     */       try {
/*  71 */         pattern.checkRecursion(0);
/*  72 */         pattern = pattern.expand(this.pb);
/*  73 */         pattern.checkRestrictions(0, null, null);
/*  74 */         if (!this.hadError) return pattern; 
/*  75 */       } catch (SAXParseException e) {
/*  76 */         error(e);
/*  77 */       } catch (SAXException e) {
/*  78 */         throw new BuildException(e);
/*  79 */       } catch (RestrictionViolationException e) {
/*  80 */         if (e.getName() != null) {
/*  81 */           error(e.getMessageId(), e.getName().toString(), e.getLocator());
/*     */         } else {
/*     */           
/*  84 */           error(e.getMessageId(), e.getLocator());
/*     */         } 
/*     */       }  
/*  87 */     throw new IllegalSchemaException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaBuilderImpl(ErrorHandler eh) {
/*  96 */     this(eh, (DatatypeLibraryFactory)new CascadingDatatypeLibraryFactory((DatatypeLibraryFactory)new DatatypeLibraryLoader(), (DatatypeLibraryFactory)new BuiltinDatatypeLibraryFactory((DatatypeLibraryFactory)new DatatypeLibraryLoader())), new SchemaPatternBuilder());
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaBuilderImpl(ErrorHandler eh, DatatypeLibraryFactory datatypeLibraryFactory, SchemaPatternBuilder pb) {
/* 114 */     this.parent = null;
/* 115 */     this.eh = eh;
/* 116 */     this.datatypeLibraryFactory = datatypeLibraryFactory;
/* 117 */     this.pb = pb;
/* 118 */     this.inheritNs = "";
/* 119 */     this.openIncludes = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SchemaBuilderImpl(String inheritNs, String uri, SchemaBuilderImpl parent) {
/* 125 */     this.parent = parent;
/* 126 */     this.eh = parent.eh;
/* 127 */     this.datatypeLibraryFactory = parent.datatypeLibraryFactory;
/* 128 */     this.pb = parent.pb;
/* 129 */     this.inheritNs = inheritNs;
/* 130 */     this.openIncludes = new OpenIncludes(uri, parent.openIncludes);
/*     */   }
/*     */   
/*     */   public NameClassBuilder getNameClassBuilder() {
/* 134 */     return this.ncb;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeChoice(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 139 */     if (patterns.isEmpty())
/* 140 */       throw new IllegalArgumentException(); 
/* 141 */     Pattern result = patterns.get(0);
/* 142 */     for (int i = 1; i < patterns.size(); i++)
/* 143 */       result = this.pb.makeChoice(result, patterns.get(i)); 
/* 144 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeInterleave(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 149 */     if (patterns.isEmpty())
/* 150 */       throw new IllegalArgumentException(); 
/* 151 */     Pattern result = patterns.get(0);
/* 152 */     for (int i = 1; i < patterns.size(); i++)
/* 153 */       result = this.pb.makeInterleave(result, patterns.get(i)); 
/* 154 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeGroup(List<Pattern> patterns, Location loc, Annotations anno) throws BuildException {
/* 159 */     if (patterns.isEmpty())
/* 160 */       throw new IllegalArgumentException(); 
/* 161 */     Pattern result = patterns.get(0);
/* 162 */     for (int i = 1; i < patterns.size(); i++)
/* 163 */       result = this.pb.makeGroup(result, patterns.get(i)); 
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOneOrMore(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 169 */     return this.pb.makeOneOrMore((Pattern)p);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeZeroOrMore(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 174 */     return this.pb.makeZeroOrMore((Pattern)p);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeOptional(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 179 */     return this.pb.makeOptional((Pattern)p);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeList(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 184 */     return this.pb.makeList((Pattern)p, (Locator)loc);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeMixed(ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 189 */     return this.pb.makeMixed((Pattern)p);
/*     */   }
/*     */   
/*     */   public ParsedPattern makeEmpty(Location loc, Annotations anno) {
/* 193 */     return this.pb.makeEmpty();
/*     */   }
/*     */   
/*     */   public ParsedPattern makeNotAllowed(Location loc, Annotations anno) {
/* 197 */     return this.pb.makeUnexpandedNotAllowed();
/*     */   }
/*     */   
/*     */   public ParsedPattern makeText(Location loc, Annotations anno) {
/* 201 */     return this.pb.makeText();
/*     */   }
/*     */   
/*     */   public ParsedPattern makeErrorPattern() {
/* 205 */     return this.pb.makeError();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeAttribute(ParsedNameClass nc, ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 214 */     return this.pb.makeAttribute((NameClass)nc, (Pattern)p, (Locator)loc);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeElement(ParsedNameClass nc, ParsedPattern p, Location loc, Annotations anno) throws BuildException {
/* 219 */     return this.pb.makeElement((NameClass)nc, (Pattern)p, (Locator)loc);
/*     */   }
/*     */   
/*     */   private class DummyDataPatternBuilder
/*     */     implements DataPatternBuilder {
/*     */     private DummyDataPatternBuilder() {}
/*     */     
/*     */     public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {}
/*     */     
/*     */     public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/* 229 */       return SchemaBuilderImpl.this.pb.makeError();
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/* 234 */       return SchemaBuilderImpl.this.pb.makeError();
/*     */     }
/*     */     
/*     */     public void annotation(ParsedElementAnnotation ea) {}
/*     */   }
/*     */   
/*     */   private class ValidationContextImpl
/*     */     implements ValidationContext {
/*     */     private ValidationContext vc;
/*     */     private String ns;
/*     */     
/*     */     ValidationContextImpl(ValidationContext vc, String ns) {
/* 246 */       this.vc = vc;
/* 247 */       this.ns = (ns.length() == 0) ? null : ns;
/*     */     }
/*     */     
/*     */     public String resolveNamespacePrefix(String prefix) {
/* 251 */       return (prefix.length() == 0) ? this.ns : this.vc.resolveNamespacePrefix(prefix);
/*     */     }
/*     */     
/*     */     public String getBaseUri() {
/* 255 */       return this.vc.getBaseUri();
/*     */     }
/*     */     
/*     */     public boolean isUnparsedEntity(String entityName) {
/* 259 */       return this.vc.isUnparsedEntity(entityName);
/*     */     }
/*     */     
/*     */     public boolean isNotation(String notationName) {
/* 263 */       return this.vc.isNotation(notationName);
/*     */     } }
/*     */   
/*     */   private class DataPatternBuilderImpl implements DataPatternBuilder {
/*     */     private DatatypeBuilder dtb;
/*     */     
/*     */     DataPatternBuilderImpl(DatatypeBuilder dtb) {
/* 270 */       this.dtb = dtb;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParam(String name, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 276 */         this.dtb.addParameter(name, value, new SchemaBuilderImpl.ValidationContextImpl((ValidationContext)context, ns));
/*     */       }
/* 278 */       catch (DatatypeException e) {
/* 279 */         String displayedParam, detail = e.getMessage();
/* 280 */         int pos = e.getIndex();
/*     */         
/* 282 */         if (pos == -1) {
/* 283 */           displayedParam = null;
/*     */         } else {
/* 285 */           displayedParam = displayParam(value, pos);
/* 286 */         }  if (displayedParam != null) {
/* 287 */           if (detail != null) {
/* 288 */             SchemaBuilderImpl.this.error("invalid_param_detail_display", detail, displayedParam, (Locator)loc);
/*     */           } else {
/* 290 */             SchemaBuilderImpl.this.error("invalid_param_display", displayedParam, (Locator)loc);
/*     */           } 
/* 292 */         } else if (detail != null) {
/* 293 */           SchemaBuilderImpl.this.error("invalid_param_detail", detail, (Locator)loc);
/*     */         } else {
/* 295 */           SchemaBuilderImpl.this.error("invalid_param", (Locator)loc);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     String displayParam(String value, int pos) {
/* 300 */       if (pos < 0) {
/* 301 */         pos = 0;
/* 302 */       } else if (pos > value.length()) {
/* 303 */         pos = value.length();
/* 304 */       }  return SchemaBuilderImpl.localizer.message("display_param", value.substring(0, pos), value.substring(pos));
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern makePattern(Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 310 */         return SchemaBuilderImpl.this.pb.makeData(this.dtb.createDatatype());
/*     */       }
/* 312 */       catch (DatatypeException e) {
/* 313 */         String detail = e.getMessage();
/* 314 */         if (detail != null) {
/* 315 */           SchemaBuilderImpl.this.error("invalid_params_detail", detail, (Locator)loc);
/*     */         } else {
/* 317 */           SchemaBuilderImpl.this.error("invalid_params", (Locator)loc);
/* 318 */         }  return SchemaBuilderImpl.this.pb.makeError();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern makePattern(ParsedPattern except, Location loc, Annotations anno) throws BuildException {
/*     */       try {
/* 325 */         return SchemaBuilderImpl.this.pb.makeDataExcept(this.dtb.createDatatype(), (Pattern)except, (Locator)loc);
/*     */       }
/* 327 */       catch (DatatypeException e) {
/* 328 */         String detail = e.getMessage();
/* 329 */         if (detail != null) {
/* 330 */           SchemaBuilderImpl.this.error("invalid_params_detail", detail, (Locator)loc);
/*     */         } else {
/* 332 */           SchemaBuilderImpl.this.error("invalid_params", (Locator)loc);
/* 333 */         }  return SchemaBuilderImpl.this.pb.makeError();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void annotation(ParsedElementAnnotation ea) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public DataPatternBuilder makeDataPatternBuilder(String datatypeLibrary, String type, Location loc) throws BuildException {
/* 343 */     DatatypeLibrary dl = this.datatypeLibraryFactory.createDatatypeLibrary(datatypeLibrary);
/* 344 */     if (dl == null) {
/* 345 */       error("unrecognized_datatype_library", datatypeLibrary, (Locator)loc);
/*     */     } else {
/*     */       try {
/* 348 */         return new DataPatternBuilderImpl(dl.createDatatypeBuilder(type));
/*     */       }
/* 350 */       catch (DatatypeException e) {
/* 351 */         String detail = e.getMessage();
/* 352 */         if (detail != null) {
/* 353 */           error("unsupported_datatype_detail", datatypeLibrary, type, detail, (Locator)loc);
/*     */         } else {
/* 355 */           error("unrecognized_datatype", datatypeLibrary, type, (Locator)loc);
/*     */         } 
/*     */       } 
/* 358 */     }  return new DummyDataPatternBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern makeValue(String datatypeLibrary, String type, String value, Context context, String ns, Location loc, Annotations anno) throws BuildException {
/* 363 */     DatatypeLibrary dl = this.datatypeLibraryFactory.createDatatypeLibrary(datatypeLibrary);
/* 364 */     if (dl == null) {
/* 365 */       error("unrecognized_datatype_library", datatypeLibrary, (Locator)loc);
/*     */     } else {
/*     */       try {
/* 368 */         DatatypeBuilder dtb = dl.createDatatypeBuilder(type);
/*     */         try {
/* 370 */           Datatype dt = dtb.createDatatype();
/* 371 */           Object obj = dt.createValue(value, new ValidationContextImpl((ValidationContext)context, ns));
/* 372 */           if (obj != null)
/* 373 */             return this.pb.makeValue(dt, obj); 
/* 374 */           error("invalid_value", value, (Locator)loc);
/*     */         }
/* 376 */         catch (DatatypeException e) {
/* 377 */           String detail = e.getMessage();
/* 378 */           if (detail != null) {
/* 379 */             error("datatype_requires_param_detail", detail, (Locator)loc);
/*     */           } else {
/* 381 */             error("datatype_requires_param", (Locator)loc);
/*     */           } 
/*     */         } 
/* 384 */       } catch (DatatypeException e) {
/* 385 */         error("unrecognized_datatype", datatypeLibrary, type, (Locator)loc);
/*     */       } 
/*     */     } 
/* 388 */     return this.pb.makeError();
/*     */   }
/*     */   
/*     */   static class GrammarImpl implements Grammar, Div, IncludedGrammar {
/*     */     private final SchemaBuilderImpl sb;
/*     */     private final Hashtable defines;
/*     */     private final RefPattern startRef;
/*     */     private final Scope parent;
/*     */     
/*     */     private GrammarImpl(SchemaBuilderImpl sb, Scope parent) {
/* 398 */       this.sb = sb;
/* 399 */       this.parent = parent;
/* 400 */       this.defines = new Hashtable<Object, Object>();
/* 401 */       this.startRef = new RefPattern(null);
/*     */     }
/*     */     
/*     */     protected GrammarImpl(SchemaBuilderImpl sb, GrammarImpl g) {
/* 405 */       this.sb = sb;
/* 406 */       this.parent = g.parent;
/* 407 */       this.startRef = g.startRef;
/* 408 */       this.defines = g.defines;
/*     */     }
/*     */     
/*     */     public ParsedPattern endGrammar(Location loc, Annotations anno) throws BuildException {
/* 412 */       Enumeration<String> e = this.defines.keys();
/* 413 */       while (e.hasMoreElements()) {
/* 414 */         String name = e.nextElement();
/* 415 */         RefPattern rp = (RefPattern)this.defines.get(name);
/* 416 */         if (rp.getPattern() == null) {
/* 417 */           this.sb.error("reference_to_undefined", name, rp.getRefLocator());
/* 418 */           rp.setPattern(this.sb.pb.makeError());
/*     */         } 
/*     */       } 
/* 421 */       Pattern start = this.startRef.getPattern();
/* 422 */       if (start == null) {
/* 423 */         this.sb.error("missing_start_element", (Locator)loc);
/* 424 */         start = this.sb.pb.makeError();
/*     */       } 
/* 426 */       return start;
/*     */     }
/*     */ 
/*     */     
/*     */     public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*     */ 
/*     */     
/*     */     public ParsedPattern endIncludedGrammar(Location loc, Annotations anno) throws BuildException {
/* 434 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 439 */       define(lookup(name), combine, pattern, loc);
/*     */     }
/*     */     
/*     */     private void define(RefPattern rp, GrammarSection.Combine combine, ParsedPattern pattern, Location loc) throws BuildException {
/*     */       Pattern p;
/* 444 */       switch (rp.getReplacementStatus()) {
/*     */         case 0:
/* 446 */           if (combine == null) {
/* 447 */             if (rp.isCombineImplicit()) {
/* 448 */               if (rp.getName() == null) {
/* 449 */                 this.sb.error("duplicate_start", (Locator)loc);
/*     */               } else {
/* 451 */                 this.sb.error("duplicate_define", rp.getName(), (Locator)loc);
/*     */               } 
/*     */             } else {
/* 454 */               rp.setCombineImplicit();
/*     */             } 
/*     */           } else {
/* 457 */             byte combineType = (combine == COMBINE_CHOICE) ? 1 : 2;
/* 458 */             if (rp.getCombineType() != 0 && rp.getCombineType() != combineType)
/*     */             {
/* 460 */               if (rp.getName() == null) {
/* 461 */                 this.sb.error("conflict_combine_start", (Locator)loc);
/*     */               } else {
/* 463 */                 this.sb.error("conflict_combine_define", rp.getName(), (Locator)loc);
/*     */               }  } 
/* 465 */             rp.setCombineType(combineType);
/*     */           } 
/* 467 */           p = (Pattern)pattern;
/* 468 */           if (rp.getPattern() == null) {
/* 469 */             rp.setPattern(p); break;
/* 470 */           }  if (rp.getCombineType() == 2) {
/* 471 */             rp.setPattern(this.sb.pb.makeInterleave(rp.getPattern(), p)); break;
/*     */           } 
/* 473 */           rp.setPattern(this.sb.pb.makeChoice(rp.getPattern(), p));
/*     */           break;
/*     */         case 1:
/* 476 */           rp.setReplacementStatus((byte)2);
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {}
/*     */ 
/*     */     
/*     */     public void topLevelComment(CommentList comments) throws BuildException {}
/*     */ 
/*     */     
/*     */     private RefPattern lookup(String name) {
/* 490 */       if (name == "\000#start\000")
/* 491 */         return this.startRef; 
/* 492 */       return lookup1(name);
/*     */     }
/*     */     
/*     */     private RefPattern lookup1(String name) {
/* 496 */       RefPattern p = (RefPattern)this.defines.get(name);
/* 497 */       if (p == null) {
/* 498 */         p = new RefPattern(name);
/* 499 */         this.defines.put(name, p);
/*     */       } 
/* 501 */       return p;
/*     */     }
/*     */     
/*     */     public ParsedPattern makeRef(String name, Location loc, Annotations anno) throws BuildException {
/* 505 */       RefPattern p = lookup1(name);
/* 506 */       if (p.getRefLocator() == null && loc != null)
/* 507 */         p.setRefLocator((Locator)loc); 
/* 508 */       return p;
/*     */     }
/*     */ 
/*     */     
/*     */     public ParsedPattern makeParentRef(String name, Location loc, Annotations anno) throws BuildException {
/* 513 */       if (this.parent == null) {
/* 514 */         this.sb.error("parent_ref_outside_grammar", (Locator)loc);
/* 515 */         return this.sb.makeErrorPattern();
/*     */       } 
/* 517 */       return this.parent.makeRef(name, loc, anno);
/*     */     }
/*     */     
/*     */     public Div makeDiv() {
/* 521 */       return this;
/*     */     }
/*     */     
/*     */     public Include makeInclude() {
/* 525 */       return new SchemaBuilderImpl.IncludeImpl(this.sb, this);
/*     */     } }
/*     */   
/*     */   static class Override { RefPattern prp;
/*     */     Override next;
/*     */     byte replacementStatus;
/*     */     
/*     */     Override(RefPattern prp, Override next) {
/* 533 */       this.prp = prp;
/* 534 */       this.next = next;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class IncludeImpl
/*     */     implements Include, Div
/*     */   {
/*     */     private SchemaBuilderImpl sb;
/*     */     
/*     */     private SchemaBuilderImpl.Override overrides;
/*     */     
/*     */     private SchemaBuilderImpl.GrammarImpl grammar;
/*     */     
/*     */     private IncludeImpl(SchemaBuilderImpl sb, SchemaBuilderImpl.GrammarImpl grammar) {
/* 549 */       this.sb = sb;
/* 550 */       this.grammar = grammar;
/*     */     }
/*     */ 
/*     */     
/*     */     public void define(String name, GrammarSection.Combine combine, ParsedPattern pattern, Location loc, Annotations anno) throws BuildException {
/* 555 */       RefPattern rp = this.grammar.lookup(name);
/* 556 */       this.overrides = new SchemaBuilderImpl.Override(rp, this.overrides);
/* 557 */       this.grammar.define(rp, combine, pattern, loc);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void endDiv(Location loc, Annotations anno) throws BuildException {}
/*     */ 
/*     */     
/*     */     public void topLevelAnnotation(ParsedElementAnnotation ea) throws BuildException {}
/*     */ 
/*     */     
/*     */     public void topLevelComment(CommentList comments) throws BuildException {}
/*     */ 
/*     */     
/*     */     public Div makeDiv() {
/* 572 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void endInclude(Parseable current, String uri, String ns, Location loc, Annotations anno) throws BuildException {
/* 577 */       SchemaBuilderImpl.OpenIncludes inc = this.sb.openIncludes;
/* 578 */       for (; inc != null; 
/* 579 */         inc = inc.parent) {
/* 580 */         if (inc.uri.equals(uri)) {
/* 581 */           this.sb.error("recursive_include", uri, (Locator)loc);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 586 */       for (SchemaBuilderImpl.Override o = this.overrides; o != null; o = o.next) {
/* 587 */         o.replacementStatus = o.prp.getReplacementStatus();
/* 588 */         o.prp.setReplacementStatus((byte)1);
/*     */       } 
/*     */       try {
/* 591 */         SchemaBuilderImpl isb = new SchemaBuilderImpl(ns, uri, this.sb);
/* 592 */         current.parseInclude(uri, isb, new SchemaBuilderImpl.GrammarImpl(isb, this.grammar), ns);
/* 593 */         for (SchemaBuilderImpl.Override override2 = this.overrides; override2 != null; override2 = override2.next) {
/* 594 */           if (override2.prp.getReplacementStatus() == 1) {
/* 595 */             if (override2.prp.getName() == null) {
/* 596 */               this.sb.error("missing_start_replacement", (Locator)loc);
/*     */             } else {
/* 598 */               this.sb.error("missing_define_replacement", override2.prp.getName(), (Locator)loc);
/*     */             } 
/*     */           }
/*     */         } 
/* 602 */       } catch (IllegalSchemaException e) {
/* 603 */         this.sb.noteError();
/*     */       } finally {
/*     */         
/* 606 */         for (SchemaBuilderImpl.Override override = this.overrides; override != null; override = override.next)
/* 607 */           override.prp.setReplacementStatus(override.replacementStatus); 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Include makeInclude() {
/* 612 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public Grammar makeGrammar(Scope parent) {
/* 617 */     return new GrammarImpl(this, parent);
/*     */   }
/*     */   
/*     */   public ParsedPattern annotate(ParsedPattern p, Annotations anno) throws BuildException {
/* 621 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public ParsedPattern annotateAfter(ParsedPattern p, ParsedElementAnnotation e) throws BuildException {
/* 626 */     return p;
/*     */   }
/*     */   
/*     */   public ParsedPattern commentAfter(ParsedPattern p, CommentList comments) throws BuildException {
/* 630 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParsedPattern makeExternalRef(Parseable current, String uri, String ns, Scope scope, Location loc, Annotations anno) throws BuildException {
/* 637 */     OpenIncludes inc = this.openIncludes;
/* 638 */     for (; inc != null; 
/* 639 */       inc = inc.parent) {
/* 640 */       if (inc.uri.equals(uri)) {
/* 641 */         error("recursive_include", uri, (Locator)loc);
/* 642 */         return this.pb.makeError();
/*     */       } 
/*     */     } 
/*     */     try {
/* 646 */       return current.parseExternal(uri, new SchemaBuilderImpl(ns, uri, this), scope, ns);
/*     */     }
/* 648 */     catch (IllegalSchemaException e) {
/* 649 */       noteError();
/* 650 */       return this.pb.makeError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Location makeLocation(String systemId, int lineNumber, int columnNumber) {
/* 657 */     return (Location)new LocatorImpl(systemId, lineNumber, columnNumber);
/*     */   }
/*     */   
/*     */   public Annotations makeAnnotations(CommentList comments, Context context) {
/* 661 */     return (Annotations)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementAnnotationBuilder makeElementAnnotationBuilder(String ns, String localName, String prefix, Location loc, CommentList comments, Context context) {
/* 666 */     return this;
/*     */   }
/*     */   
/*     */   public CommentList makeCommentList() {
/* 670 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComment(String value, Location loc) throws BuildException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAttribute(String ns, String localName, String prefix, String value, Location loc) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElement(ParsedElementAnnotation ea) {}
/*     */ 
/*     */   
/*     */   public void addComment(CommentList comments) throws BuildException {}
/*     */ 
/*     */   
/*     */   public void addLeadingComment(CommentList comments) throws BuildException {}
/*     */ 
/*     */   
/*     */   public ParsedElementAnnotation makeElementAnnotation() {
/* 693 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addText(String value, Location loc, CommentList comments) throws BuildException {}
/*     */   
/*     */   public boolean usesComments() {
/* 700 */     return false;
/*     */   }
/*     */   
/*     */   private void error(SAXParseException message) throws BuildException {
/* 704 */     noteError();
/*     */     try {
/* 706 */       if (this.eh != null) {
/* 707 */         this.eh.error(message);
/*     */       }
/* 709 */     } catch (SAXException e) {
/* 710 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void warning(SAXParseException message) throws BuildException {
/*     */     try {
/* 716 */       if (this.eh != null) {
/* 717 */         this.eh.warning(message);
/*     */       }
/* 719 */     } catch (SAXException e) {
/* 720 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void error(String key, Locator loc) throws BuildException {
/* 725 */     error(new SAXParseException(localizer.message(key), loc));
/*     */   }
/*     */   
/*     */   private void error(String key, String arg, Locator loc) throws BuildException {
/* 729 */     error(new SAXParseException(localizer.message(key, arg), loc));
/*     */   }
/*     */   
/*     */   private void error(String key, String arg1, String arg2, Locator loc) throws BuildException {
/* 733 */     error(new SAXParseException(localizer.message(key, arg1, arg2), loc));
/*     */   }
/*     */   
/*     */   private void error(String key, String arg1, String arg2, String arg3, Locator loc) throws BuildException {
/* 737 */     error(new SAXParseException(localizer.message(key, new Object[] { arg1, arg2, arg3 }), loc));
/*     */   }
/*     */   private void noteError() {
/* 740 */     if (!this.hadError && this.parent != null)
/* 741 */       this.parent.noteError(); 
/* 742 */     this.hadError = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\SchemaBuilderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
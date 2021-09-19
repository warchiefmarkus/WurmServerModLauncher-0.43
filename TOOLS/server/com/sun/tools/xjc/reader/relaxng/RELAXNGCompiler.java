/*     */ package com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfoParent;
/*     */ import com.sun.tools.xjc.model.CEnumConstant;
/*     */ import com.sun.tools.xjc.model.CEnumLeafInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.xjc.model.Model;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.xml.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.kohsuke.rngom.digested.DChoicePattern;
/*     */ import org.kohsuke.rngom.digested.DDefine;
/*     */ import org.kohsuke.rngom.digested.DElementPattern;
/*     */ import org.kohsuke.rngom.digested.DPattern;
/*     */ import org.kohsuke.rngom.digested.DPatternVisitor;
/*     */ import org.kohsuke.rngom.digested.DPatternWalker;
/*     */ import org.kohsuke.rngom.digested.DRefPattern;
/*     */ import org.kohsuke.rngom.digested.DValuePattern;
/*     */ import org.kohsuke.rngom.nc.NameClass;
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
/*     */ public final class RELAXNGCompiler
/*     */ {
/*     */   final DPattern grammar;
/*     */   final Set<DDefine> defs;
/*     */   final Options opts;
/*     */   final Model model;
/*     */   final JPackage pkg;
/*  94 */   final Map<String, DatatypeLib> datatypes = new HashMap<String, DatatypeLib>();
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
/* 106 */   final Map<DPattern, CTypeInfo[]> classes = (Map)new HashMap<DPattern, CTypeInfo>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   final Map<CClassInfo, DPattern> bindQueue = new HashMap<CClassInfo, DPattern>();
/*     */   
/* 115 */   final TypeUseBinder typeUseBinder = new TypeUseBinder(this);
/*     */   
/*     */   public static Model build(DPattern grammar, JCodeModel codeModel, Options opts) {
/* 118 */     RELAXNGCompiler compiler = new RELAXNGCompiler(grammar, codeModel, opts);
/* 119 */     compiler.compile();
/* 120 */     return compiler.model;
/*     */   }
/*     */   
/*     */   public RELAXNGCompiler(DPattern grammar, JCodeModel codeModel, Options opts) {
/* 124 */     this.grammar = grammar;
/* 125 */     this.opts = opts;
/* 126 */     this.model = new Model(opts, codeModel, NameConverter.smart, opts.classNameAllocator, null);
/*     */     
/* 128 */     this.datatypes.put("", DatatypeLib.BUILTIN);
/* 129 */     this.datatypes.put("http://www.w3.org/2001/XMLSchema-datatypes", DatatypeLib.XMLSCHEMA);
/*     */ 
/*     */     
/* 132 */     DefineFinder deff = new DefineFinder();
/* 133 */     grammar.accept((DPatternVisitor)deff);
/* 134 */     this.defs = deff.defs;
/*     */     
/* 136 */     if (opts.defaultPackage2 != null) {
/* 137 */       this.pkg = codeModel._package(opts.defaultPackage2);
/*     */     }
/* 139 */     else if (opts.defaultPackage != null) {
/* 140 */       this.pkg = codeModel._package(opts.defaultPackage);
/*     */     } else {
/* 142 */       this.pkg = codeModel.rootPackage();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void compile() {
/* 147 */     promoteElementDefsToClasses();
/* 148 */     promoteTypeSafeEnums();
/*     */ 
/*     */     
/* 151 */     promoteTypePatternsToClasses();
/*     */     
/* 153 */     for (Map.Entry<CClassInfo, DPattern> e : this.bindQueue.entrySet()) {
/* 154 */       bindContentModel(e.getKey(), e.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void bindContentModel(CClassInfo clazz, DPattern pattern) {
/* 161 */     pattern.accept((DPatternVisitor)new ContentModelBinder(this, clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void promoteTypeSafeEnums() {
/* 168 */     List<CEnumConstant> members = new ArrayList<CEnumConstant>();
/*     */ 
/*     */     
/* 171 */     label32: for (DDefine def : this.defs) {
/* 172 */       DPattern p = def.getPattern();
/* 173 */       if (p instanceof DChoicePattern) {
/* 174 */         CNonElement cNonElement; DChoicePattern cp = (DChoicePattern)p;
/*     */         
/* 176 */         members.clear();
/*     */ 
/*     */ 
/*     */         
/* 180 */         DValuePattern vp = null;
/*     */         
/* 182 */         for (DPattern child : cp) {
/* 183 */           if (child instanceof DValuePattern) {
/* 184 */             DValuePattern c = (DValuePattern)child;
/* 185 */             if (vp == null)
/* 186 */             { vp = c; }
/*     */             
/* 188 */             else if (vp.getDatatypeLibrary().equals(c.getDatatypeLibrary())) { if (!vp.getType().equals(c.getType()))
/*     */                 continue label32;  }
/*     */             else
/*     */             { continue label32; }
/*     */             
/* 193 */             members.add(new CEnumConstant(this.model.getNameConverter().toConstantName(c.getValue()), null, c.getValue(), c.getLocation()));
/*     */             
/*     */             continue;
/*     */           } 
/*     */           
/*     */           continue label32;
/*     */         } 
/*     */         
/* 201 */         if (members.isEmpty()) {
/*     */           continue;
/*     */         }
/* 204 */         CBuiltinLeafInfo cBuiltinLeafInfo = CBuiltinLeafInfo.STRING;
/*     */         
/* 206 */         DatatypeLib lib = this.datatypes.get(vp.getNs());
/* 207 */         if (lib != null) {
/* 208 */           TypeUse use = lib.get(vp.getType());
/* 209 */           if (use instanceof CNonElement) {
/* 210 */             cNonElement = (CNonElement)use;
/*     */           }
/*     */         } 
/* 213 */         CEnumLeafInfo xducer = new CEnumLeafInfo(this.model, null, (CClassInfoParent)new CClassInfoParent.Package(this.pkg), def.getName(), cNonElement, new ArrayList<CEnumConstant>(members), null, null, cp.getLocation());
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 218 */         this.classes.put(cp, new CTypeInfo[] { (CTypeInfo)xducer });
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void promoteElementDefsToClasses() {
/* 226 */     for (DDefine def : this.defs) {
/* 227 */       DPattern p = def.getPattern();
/* 228 */       if (p instanceof DElementPattern) {
/* 229 */         DElementPattern ep = (DElementPattern)p;
/*     */         
/* 231 */         mapToClass(ep);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 236 */     this.grammar.accept((DPatternVisitor)new DPatternWalker() {
/*     */           public Void onRef(DRefPattern p) {
/* 238 */             return null;
/*     */           }
/*     */           
/*     */           public Void onElement(DElementPattern p) {
/* 242 */             RELAXNGCompiler.this.mapToClass(p);
/* 243 */             return null;
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void mapToClass(DElementPattern p) {
/* 249 */     NameClass nc = p.getName();
/* 250 */     if (nc.isOpen()) {
/*     */       return;
/*     */     }
/* 253 */     Set<QName> names = nc.listNames();
/*     */     
/* 255 */     CClassInfo[] types = new CClassInfo[names.size()];
/* 256 */     int i = 0;
/* 257 */     for (QName n : names) {
/*     */       
/* 259 */       String name = this.model.getNameConverter().toClassName(n.getLocalPart());
/*     */       
/* 261 */       this.bindQueue.put(types[i++] = new CClassInfo(this.model, this.pkg, name, p.getLocation(), null, n, null, null), p.getChild());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 266 */     this.classes.put(p, types);
/*     */   }
/*     */   
/*     */   private void promoteTypePatternsToClasses() {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\RELAXNGCompiler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
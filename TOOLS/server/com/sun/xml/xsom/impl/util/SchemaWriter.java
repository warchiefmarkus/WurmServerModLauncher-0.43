/*     */ package com.sun.xml.xsom.impl.util;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSListSimpleType;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSRestrictionSimpleType;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSUnionSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSSimpleTypeVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
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
/*     */ public class SchemaWriter
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   private final Writer out;
/*     */   private int indent;
/*     */   private boolean hadError;
/*     */   
/*     */   public SchemaWriter(Writer _out) {
/*  96 */     this.hadError = false; this.out = _out;
/*     */   } private void println(String s) { try { for (int i = 0; i < this.indent; ) { this.out.write("  "); i++; }
/*     */        this.out.write(s); this.out.write(10); this.out.flush(); }
/*     */     catch (IOException e) { this.hadError = true; }
/*     */      } public boolean checkError() { try {
/* 101 */       this.out.flush();
/* 102 */     } catch (IOException e) {
/* 103 */       this.hadError = true;
/*     */     } 
/* 105 */     return this.hadError; }
/*     */    private void println() {
/*     */     println("");
/*     */   } public void visit(XSSchemaSet s) {
/* 109 */     Iterator<XSSchema> itr = s.iterateSchema();
/* 110 */     while (itr.hasNext()) {
/* 111 */       schema(itr.next());
/* 112 */       println();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void schema(XSSchema s) {
/* 119 */     if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/*     */       return;
/*     */     }
/* 122 */     println(MessageFormat.format("<schema targetNamespace=\"{0}\">", new Object[] { s.getTargetNamespace() }));
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 130 */     Iterator<XSAttGroupDecl> itr = s.iterateAttGroupDecls();
/* 131 */     while (itr.hasNext()) {
/* 132 */       attGroupDecl(itr.next());
/*     */     }
/* 134 */     itr = s.iterateAttributeDecls();
/* 135 */     while (itr.hasNext()) {
/* 136 */       attributeDecl((XSAttributeDecl)itr.next());
/*     */     }
/* 138 */     itr = s.iterateComplexTypes();
/* 139 */     while (itr.hasNext()) {
/* 140 */       complexType((XSComplexType)itr.next());
/*     */     }
/* 142 */     itr = s.iterateElementDecls();
/* 143 */     while (itr.hasNext()) {
/* 144 */       elementDecl((XSElementDecl)itr.next());
/*     */     }
/* 146 */     itr = s.iterateModelGroupDecls();
/* 147 */     while (itr.hasNext()) {
/* 148 */       modelGroupDecl((XSModelGroupDecl)itr.next());
/*     */     }
/* 150 */     itr = s.iterateSimpleTypes();
/* 151 */     while (itr.hasNext()) {
/* 152 */       simpleType((XSSimpleType)itr.next());
/*     */     }
/* 154 */     this.indent--;
/* 155 */     println("</schema>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 161 */     println(MessageFormat.format("<attGroup name=\"{0}\">", new Object[] { decl.getName() }));
/*     */     
/* 163 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 167 */     Iterator<XSAttGroupDecl> itr = decl.iterateAttGroups();
/* 168 */     while (itr.hasNext()) {
/* 169 */       dumpRef(itr.next());
/*     */     }
/* 171 */     itr = decl.iterateDeclaredAttributeUses();
/* 172 */     while (itr.hasNext()) {
/* 173 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/* 175 */     this.indent--;
/* 176 */     println("</attGroup>");
/*     */   }
/*     */   
/*     */   public void dumpRef(XSAttGroupDecl decl) {
/* 180 */     println(MessageFormat.format("<attGroup ref=\"'{'{0}'}'{1}\"/>", new Object[] { decl.getTargetNamespace(), decl.getName() }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 185 */     XSAttributeDecl decl = use.getDecl();
/*     */     
/* 187 */     String additionalAtts = "";
/*     */     
/* 189 */     if (use.isRequired())
/* 190 */       additionalAtts = additionalAtts + " use=\"required\""; 
/* 191 */     if (use.getFixedValue() != null && use.getDecl().getFixedValue() == null)
/* 192 */       additionalAtts = additionalAtts + " fixed=\"" + use.getFixedValue() + '"'; 
/* 193 */     if (use.getDefaultValue() != null && use.getDecl().getDefaultValue() == null) {
/* 194 */       additionalAtts = additionalAtts + " default=\"" + use.getDefaultValue() + '"';
/*     */     }
/* 196 */     if (decl.isLocal()) {
/*     */       
/* 198 */       dump(decl, additionalAtts);
/*     */     } else {
/*     */       
/* 201 */       println(MessageFormat.format("<attribute ref=\"'{'{0}'}'{1}{2}\"/>", new Object[] { decl.getTargetNamespace(), decl.getName(), additionalAtts }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 208 */     dump(decl, "");
/*     */   }
/*     */   
/*     */   private void dump(XSAttributeDecl decl, String additionalAtts) {
/* 212 */     XSSimpleType type = decl.getType();
/*     */     
/* 214 */     println(MessageFormat.format("<attribute name=\"{0}\"{1}{2}{3}{4}{5}>", new Object[] { decl.getName(), additionalAtts, type.isLocal() ? "" : MessageFormat.format(" type=\"'{'{0}'}'{1}\"", new Object[] { type.getTargetNamespace(), type.getName() }), (decl.getFixedValue() == null) ? "" : (" fixed=\"" + decl.getFixedValue() + '"'), (decl.getDefaultValue() == null) ? "" : (" default=\"" + decl.getDefaultValue() + '"'), type.isLocal() ? "" : " /" }));
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
/* 231 */     if (type.isLocal()) {
/* 232 */       this.indent++;
/* 233 */       simpleType(type);
/* 234 */       this.indent--;
/* 235 */       println("</attribute>");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/* 240 */     println(MessageFormat.format("<simpleType{0}>", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + '"') }));
/*     */ 
/*     */ 
/*     */     
/* 244 */     this.indent++;
/*     */     
/* 246 */     type.visit(this);
/*     */     
/* 248 */     this.indent--;
/* 249 */     println("</simpleType>");
/*     */   }
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 253 */     XSSimpleType itemType = type.getItemType();
/*     */     
/* 255 */     if (itemType.isLocal()) {
/* 256 */       println("<list>");
/* 257 */       this.indent++;
/* 258 */       simpleType(itemType);
/* 259 */       this.indent--;
/* 260 */       println("</list>");
/*     */     } else {
/*     */       
/* 263 */       println(MessageFormat.format("<list itemType=\"'{'{0}'}'{1}\" />", new Object[] { itemType.getTargetNamespace(), itemType.getName() }));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 272 */     int len = type.getMemberSize();
/* 273 */     StringBuffer ref = new StringBuffer();
/*     */     int i;
/* 275 */     for (i = 0; i < len; i++) {
/* 276 */       XSSimpleType member = type.getMember(i);
/* 277 */       if (member.isGlobal()) {
/* 278 */         ref.append(MessageFormat.format(" '{'{0}'}'{1}", new Object[] { member.getTargetNamespace(), member.getName() }));
/*     */       }
/*     */     } 
/*     */     
/* 282 */     if (ref.length() == 0) {
/* 283 */       println("<union>");
/*     */     } else {
/* 285 */       println("<union memberTypes=\"" + ref + "\">");
/* 286 */     }  this.indent++;
/*     */     
/* 288 */     for (i = 0; i < len; i++) {
/* 289 */       XSSimpleType member = type.getMember(i);
/* 290 */       if (member.isLocal())
/* 291 */         simpleType(member); 
/*     */     } 
/* 293 */     this.indent--;
/* 294 */     println("</union>");
/*     */   }
/*     */ 
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 299 */     if (type.getBaseType() == null) {
/*     */       
/* 301 */       if (!type.getName().equals("anySimpleType"))
/* 302 */         throw new InternalError(); 
/* 303 */       if (!"http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/* 304 */         throw new InternalError();
/*     */       }
/*     */       return;
/*     */     } 
/* 308 */     XSSimpleType baseType = type.getSimpleBaseType();
/*     */     
/* 310 */     println(MessageFormat.format("<restriction{0}>", new Object[] { baseType.isLocal() ? "" : (" base=\"{" + baseType.getTargetNamespace() + '}' + baseType.getName() + '"') }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     this.indent++;
/*     */     
/* 318 */     if (baseType.isLocal()) {
/* 319 */       simpleType(baseType);
/*     */     }
/* 321 */     Iterator<XSFacet> itr = type.iterateDeclaredFacets();
/* 322 */     while (itr.hasNext()) {
/* 323 */       facet(itr.next());
/*     */     }
/* 325 */     this.indent--;
/* 326 */     println("</restriction>");
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 330 */     println(MessageFormat.format("<{0} value=\"{1}\"/>", new Object[] { facet.getName(), facet.getValue() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 337 */     println(MessageFormat.format("<notation name='\"0}\" public =\"{1}\" system=\"{2}\" />", new Object[] { notation.getName(), notation.getPublicId(), notation.getSystemId() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 347 */     println(MessageFormat.format("<complexType{0}>", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + '"') }));
/*     */ 
/*     */ 
/*     */     
/* 351 */     this.indent++;
/*     */ 
/*     */ 
/*     */     
/* 355 */     if (type.getContentType().asSimpleType() != null) {
/*     */       
/* 357 */       println("<simpleContent>");
/* 358 */       this.indent++;
/*     */       
/* 360 */       XSType baseType = type.getBaseType();
/*     */       
/* 362 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 364 */         println(MessageFormat.format("<restriction base=\"<{0}>{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 368 */         this.indent++;
/*     */         
/* 370 */         dumpComplexTypeAttribute(type);
/*     */         
/* 372 */         this.indent--;
/* 373 */         println("</restriction>");
/*     */       } else {
/*     */         
/* 376 */         println(MessageFormat.format("<extension base=\"<{0}>{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 382 */         if (type.isGlobal() && type.getTargetNamespace().equals(baseType.getTargetNamespace()) && type.getName().equals(baseType.getName())) {
/*     */ 
/*     */           
/* 385 */           this.indent++;
/* 386 */           println("<redefine>");
/* 387 */           this.indent++;
/* 388 */           baseType.visit(this);
/* 389 */           this.indent--;
/* 390 */           println("</redefine>");
/* 391 */           this.indent--;
/*     */         } 
/*     */         
/* 394 */         this.indent++;
/*     */         
/* 396 */         dumpComplexTypeAttribute(type);
/*     */         
/* 398 */         this.indent--;
/* 399 */         println("</extension>");
/*     */       } 
/*     */       
/* 402 */       this.indent--;
/* 403 */       println("</simpleContent>");
/*     */     } else {
/*     */       
/* 406 */       println("<complexContent>");
/* 407 */       this.indent++;
/*     */       
/* 409 */       XSComplexType baseType = type.getBaseType().asComplexType();
/*     */       
/* 411 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 413 */         println(MessageFormat.format("<restriction base=\"'{'{0}'}'{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */         
/* 417 */         this.indent++;
/*     */         
/* 419 */         type.getContentType().visit(this);
/* 420 */         dumpComplexTypeAttribute(type);
/*     */         
/* 422 */         this.indent--;
/* 423 */         println("</restriction>");
/*     */       } else {
/*     */         
/* 426 */         println(MessageFormat.format("<extension base=\"'{'{0}'}'{1}\">", new Object[] { baseType.getTargetNamespace(), baseType.getName() }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 432 */         if (type.isGlobal() && type.getTargetNamespace().equals(baseType.getTargetNamespace()) && type.getName().equals(baseType.getName())) {
/*     */ 
/*     */           
/* 435 */           this.indent++;
/* 436 */           println("<redefine>");
/* 437 */           this.indent++;
/* 438 */           baseType.visit(this);
/* 439 */           this.indent--;
/* 440 */           println("</redefine>");
/* 441 */           this.indent--;
/*     */         } 
/*     */         
/* 444 */         this.indent++;
/*     */         
/* 446 */         type.getExplicitContent().visit(this);
/* 447 */         dumpComplexTypeAttribute(type);
/*     */         
/* 449 */         this.indent--;
/* 450 */         println("</extension>");
/*     */       } 
/*     */       
/* 453 */       this.indent--;
/* 454 */       println("</complexContent>");
/*     */     } 
/*     */     
/* 457 */     this.indent--;
/* 458 */     println("</complexType>");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpComplexTypeAttribute(XSComplexType type) {
/* 464 */     Iterator<XSAttGroupDecl> itr = type.iterateAttGroups();
/* 465 */     while (itr.hasNext()) {
/* 466 */       dumpRef(itr.next());
/*     */     }
/* 468 */     itr = type.iterateDeclaredAttributeUses();
/* 469 */     while (itr.hasNext())
/* 470 */       attributeUse((XSAttributeUse)itr.next()); 
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 474 */     elementDecl(decl, "");
/*     */   }
/*     */   private void elementDecl(XSElementDecl decl, String extraAtts) {
/* 477 */     XSType type = decl.getType();
/*     */ 
/*     */ 
/*     */     
/* 481 */     println(MessageFormat.format("<element name=\"{0}\"{1}{2}{3}>", new Object[] { decl.getName(), type.isLocal() ? "" : (" type=\"{" + type.getTargetNamespace() + '}' + type.getName() + '"'), extraAtts, type.isLocal() ? "" : "/" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 491 */     if (type.isLocal()) {
/* 492 */       this.indent++;
/*     */       
/* 494 */       if (type.isLocal()) type.visit(this);
/*     */       
/* 496 */       this.indent--;
/* 497 */       println("</element>");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 502 */     println(MessageFormat.format("<group name=\"{0}\">", new Object[] { decl.getName() }));
/*     */ 
/*     */ 
/*     */     
/* 506 */     this.indent++;
/*     */     
/* 508 */     modelGroup(decl.getModelGroup());
/*     */     
/* 510 */     this.indent--;
/* 511 */     println("</group>");
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 515 */     modelGroup(group, "");
/*     */   }
/*     */   private void modelGroup(XSModelGroup group, String extraAtts) {
/* 518 */     println(MessageFormat.format("<{0}{1}>", new Object[] { group.getCompositor(), extraAtts }));
/*     */     
/* 520 */     this.indent++;
/*     */     
/* 522 */     int len = group.getSize();
/* 523 */     for (int i = 0; i < len; i++) {
/* 524 */       particle(group.getChild(i));
/*     */     }
/* 526 */     this.indent--;
/* 527 */     println(MessageFormat.format("</{0}>", new Object[] { group.getCompositor() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void particle(XSParticle part) {
/* 534 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 536 */     int i = part.getMaxOccurs();
/* 537 */     if (i == -1) {
/* 538 */       buf.append(" maxOccurs=\"unbounded\"");
/* 539 */     } else if (i != 1) {
/* 540 */       buf.append(" maxOccurs=\"" + i + '"');
/*     */     } 
/* 542 */     i = part.getMinOccurs();
/* 543 */     if (i != 1) {
/* 544 */       buf.append(" minOccurs=\"" + i + '"');
/*     */     }
/* 546 */     final String extraAtts = buf.toString();
/*     */     
/* 548 */     part.getTerm().visit(new XSTermVisitor() {
/*     */           public void elementDecl(XSElementDecl decl) {
/* 550 */             if (decl.isLocal()) {
/* 551 */               SchemaWriter.this.elementDecl(decl, extraAtts);
/*     */             } else {
/*     */               
/* 554 */               SchemaWriter.this.println(MessageFormat.format("<element ref=\"'{'{0}'}'{1}\"{2}/>", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }));
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void modelGroupDecl(XSModelGroupDecl decl) {
/* 564 */             SchemaWriter.this.println(MessageFormat.format("<group ref=\"'{'{0}'}'{1}\"{2}/>", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }));
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void modelGroup(XSModelGroup group) {
/* 572 */             SchemaWriter.this.modelGroup(group, extraAtts);
/*     */           }
/*     */           public void wildcard(XSWildcard wc) {
/* 575 */             SchemaWriter.this.wildcard(wc, extraAtts);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 581 */     wildcard(wc, "");
/*     */   }
/*     */ 
/*     */   
/*     */   private void wildcard(XSWildcard wc, String extraAtts) {
/* 586 */     println(MessageFormat.format("<any/>", new Object[] { extraAtts }));
/*     */   }
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint decl) {}
/*     */   
/*     */   public void xpath(XSXPath xp) {}
/*     */   
/*     */   public void empty(XSContentType t) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\imp\\util\SchemaWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
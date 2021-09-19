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
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.tree.DefaultMutableTreeNode;
/*     */ import javax.swing.tree.DefaultTreeModel;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import org.xml.sax.Locator;
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
/*     */ public class SchemaTreeTraverser
/*     */   implements XSVisitor, XSSimpleTypeVisitor
/*     */ {
/*     */   public static final class SchemaTreeModel
/*     */     extends DefaultTreeModel
/*     */   {
/*     */     private SchemaTreeModel(SchemaTreeTraverser.SchemaRootNode root) {
/*  88 */       super(root);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static SchemaTreeModel getInstance() {
/*  97 */       SchemaTreeTraverser.SchemaRootNode root = new SchemaTreeTraverser.SchemaRootNode();
/*  98 */       return new SchemaTreeModel(root);
/*     */     }
/*     */     
/*     */     public void addSchemaNode(SchemaTreeTraverser.SchemaTreeNode node) {
/* 102 */       ((SchemaTreeTraverser.SchemaRootNode)this.root).add(node);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SchemaTreeNode
/*     */     extends DefaultMutableTreeNode
/*     */   {
/*     */     private String fileName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int lineNumber;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String artifactName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SchemaTreeNode(String artifactName, Locator locator) {
/* 134 */       this.artifactName = artifactName;
/* 135 */       if (locator == null) {
/* 136 */         this.fileName = null;
/*     */       } else {
/*     */         
/* 139 */         String filename = locator.getSystemId();
/* 140 */         filename = filename.replaceAll("%20", " ");
/*     */         
/* 142 */         if (filename.startsWith("file:/")) {
/* 143 */           filename = filename.substring(6);
/*     */         }
/*     */         
/* 146 */         this.fileName = filename;
/* 147 */         this.lineNumber = locator.getLineNumber() - 1;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getCaption() {
/* 157 */       return this.artifactName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getFileName() {
/* 164 */       return this.fileName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFileName(String fileName) {
/* 172 */       this.fileName = fileName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getLineNumber() {
/* 180 */       return this.lineNumber;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setLineNumber(int lineNumber) {
/* 188 */       this.lineNumber = lineNumber;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SchemaRootNode
/*     */     extends SchemaTreeNode
/*     */   {
/*     */     public SchemaRootNode() {
/* 202 */       super("Schema set", (Locator)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SchemaTreeCellRenderer
/*     */     extends JPanel
/*     */     implements TreeCellRenderer
/*     */   {
/*     */     protected final JLabel iconLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final JLabel nameLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean isSelected;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 232 */     public final Color selectedBackground = new Color(255, 244, 232);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 239 */     public final Color selectedForeground = new Color(64, 32, 0);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     public final Font nameFont = new Font("Arial", 1, 12);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SchemaTreeCellRenderer() {
/* 251 */       FlowLayout fl = new FlowLayout(0, 1, 1);
/* 252 */       setLayout(fl);
/* 253 */       this.iconLabel = new JLabel();
/* 254 */       this.iconLabel.setOpaque(false);
/* 255 */       this.iconLabel.setBorder((Border)null);
/* 256 */       add(this.iconLabel);
/*     */ 
/*     */       
/* 259 */       add(Box.createHorizontalStrut(5));
/*     */       
/* 261 */       this.nameLabel = new JLabel();
/* 262 */       this.nameLabel.setOpaque(false);
/* 263 */       this.nameLabel.setBorder((Border)null);
/* 264 */       this.nameLabel.setFont(this.nameFont);
/* 265 */       add(this.nameLabel);
/*     */       
/* 267 */       this.isSelected = false;
/*     */       
/* 269 */       setOpaque(false);
/* 270 */       setBorder((Border)null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final void paintComponent(Graphics g) {
/* 279 */       int width = getWidth();
/* 280 */       int height = getHeight();
/* 281 */       if (this.isSelected) {
/* 282 */         g.setColor(this.selectedBackground);
/* 283 */         g.fillRect(0, 0, width - 1, height - 1);
/* 284 */         g.setColor(this.selectedForeground);
/* 285 */         g.drawRect(0, 0, width - 1, height - 1);
/*     */       } 
/* 287 */       super.paintComponent(g);
/*     */     }
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
/*     */     protected final void setValues(Icon icon, String caption, boolean selected) {
/* 302 */       this.iconLabel.setIcon(icon);
/* 303 */       this.nameLabel.setText(caption);
/*     */       
/* 305 */       this.isSelected = selected;
/* 306 */       if (selected) {
/* 307 */         this.nameLabel.setForeground(this.selectedForeground);
/*     */       } else {
/*     */         
/* 310 */         this.nameLabel.setForeground(Color.black);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
/* 320 */       if (value instanceof SchemaTreeTraverser.SchemaTreeNode) {
/* 321 */         SchemaTreeTraverser.SchemaTreeNode stn = (SchemaTreeTraverser.SchemaTreeNode)value;
/*     */         
/* 323 */         setValues((Icon)null, stn.getCaption(), selected);
/* 324 */         return this;
/*     */       } 
/* 326 */       throw new IllegalStateException("Unknown node");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 335 */   private SchemaTreeModel model = SchemaTreeModel.getInstance();
/* 336 */   private SchemaTreeNode currNode = (SchemaTreeNode)this.model.getRoot();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SchemaTreeModel getModel() {
/* 345 */     return this.model;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void visit(XSSchemaSet s) {
/* 354 */     for (XSSchema schema : s.getSchemas()) {
/* 355 */       schema(schema);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void schema(XSSchema s) {
/* 364 */     if (s.getTargetNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
/*     */       return;
/*     */     }
/*     */     
/* 368 */     SchemaTreeNode newNode = new SchemaTreeNode("Schema " + s.getLocator().getSystemId(), s.getLocator());
/*     */     
/* 370 */     this.currNode = newNode;
/* 371 */     this.model.addSchemaNode(newNode);
/*     */     
/* 373 */     for (XSAttGroupDecl groupDecl : s.getAttGroupDecls().values()) {
/* 374 */       attGroupDecl(groupDecl);
/*     */     }
/*     */     
/* 377 */     for (XSAttributeDecl attrDecl : s.getAttributeDecls().values()) {
/* 378 */       attributeDecl(attrDecl);
/*     */     }
/*     */     
/* 381 */     for (XSComplexType complexType : s.getComplexTypes().values()) {
/* 382 */       complexType(complexType);
/*     */     }
/*     */     
/* 385 */     for (XSElementDecl elementDecl : s.getElementDecls().values()) {
/* 386 */       elementDecl(elementDecl);
/*     */     }
/*     */     
/* 389 */     for (XSModelGroupDecl modelGroupDecl : s.getModelGroupDecls().values()) {
/* 390 */       modelGroupDecl(modelGroupDecl);
/*     */     }
/*     */     
/* 393 */     for (XSSimpleType simpleType : s.getSimpleTypes().values()) {
/* 394 */       simpleType(simpleType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {
/* 402 */     SchemaTreeNode newNode = new SchemaTreeNode("Attribute group \"" + decl.getName() + "\"", decl.getLocator());
/*     */     
/* 404 */     this.currNode.add(newNode);
/* 405 */     this.currNode = newNode;
/*     */ 
/*     */ 
/*     */     
/* 409 */     Iterator<XSAttGroupDecl> itr = decl.iterateAttGroups();
/* 410 */     while (itr.hasNext()) {
/* 411 */       dumpRef(itr.next());
/*     */     }
/*     */     
/* 414 */     itr = decl.iterateDeclaredAttributeUses();
/* 415 */     while (itr.hasNext()) {
/* 416 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/*     */     
/* 419 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void dumpRef(XSAttGroupDecl decl) {
/* 428 */     SchemaTreeNode newNode = new SchemaTreeNode("Attribute group ref \"{" + decl.getTargetNamespace() + "}" + decl.getName() + "\"", decl.getLocator());
/*     */ 
/*     */     
/* 431 */     this.currNode.add(newNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 438 */     XSAttributeDecl decl = use.getDecl();
/*     */     
/* 440 */     String additionalAtts = "";
/*     */     
/* 442 */     if (use.isRequired()) {
/* 443 */       additionalAtts = additionalAtts + " use=\"required\"";
/*     */     }
/* 445 */     if (use.getFixedValue() != null && use.getDecl().getFixedValue() == null)
/*     */     {
/* 447 */       additionalAtts = additionalAtts + " fixed=\"" + use.getFixedValue() + "\"";
/*     */     }
/* 449 */     if (use.getDefaultValue() != null && use.getDecl().getDefaultValue() == null)
/*     */     {
/* 451 */       additionalAtts = additionalAtts + " default=\"" + use.getDefaultValue() + "\"";
/*     */     }
/*     */     
/* 454 */     if (decl.isLocal()) {
/*     */       
/* 456 */       dump(decl, additionalAtts);
/*     */     }
/*     */     else {
/*     */       
/* 460 */       String str = MessageFormat.format("Attribute ref \"'{'{0}'}'{1}{2}\"", new Object[] { decl.getTargetNamespace(), decl.getName(), additionalAtts });
/*     */ 
/*     */ 
/*     */       
/* 464 */       SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 465 */       this.currNode.add(newNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {
/* 473 */     dump(decl, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dump(XSAttributeDecl decl, String additionalAtts) {
/* 483 */     XSSimpleType type = decl.getType();
/*     */     
/* 485 */     String str = MessageFormat.format("Attribute \"{0}\"{1}{2}{3}{4}", new Object[] { decl.getName(), additionalAtts, type.isLocal() ? "" : MessageFormat.format(" type=\"'{'{0}'}'{1}\"", new Object[] { type.getTargetNamespace(), type.getName() }), (decl.getFixedValue() == null) ? "" : (" fixed=\"" + decl.getFixedValue() + "\""), (decl.getDefaultValue() == null) ? "" : (" default=\"" + decl.getDefaultValue() + "\"") });
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
/* 498 */     SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 499 */     this.currNode.add(newNode);
/* 500 */     this.currNode = newNode;
/*     */     
/* 502 */     if (type.isLocal()) {
/* 503 */       simpleType(type);
/*     */     }
/* 505 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/* 513 */     String str = MessageFormat.format("Simple type {0}", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + "\"") });
/*     */ 
/*     */ 
/*     */     
/* 517 */     SchemaTreeNode newNode = new SchemaTreeNode(str, type.getLocator());
/* 518 */     this.currNode.add(newNode);
/* 519 */     this.currNode = newNode;
/*     */     
/* 521 */     type.visit(this);
/*     */     
/* 523 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listSimpleType(XSListSimpleType type) {
/* 530 */     XSSimpleType itemType = type.getItemType();
/*     */     
/* 532 */     if (itemType.isLocal()) {
/* 533 */       SchemaTreeNode newNode = new SchemaTreeNode("List", type.getLocator());
/*     */       
/* 535 */       this.currNode.add(newNode);
/* 536 */       this.currNode = newNode;
/* 537 */       simpleType(itemType);
/* 538 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */     }
/*     */     else {
/*     */       
/* 542 */       String str = MessageFormat.format("List itemType=\"'{'{0}'}'{1}\"", new Object[] { itemType.getTargetNamespace(), itemType.getName() });
/*     */ 
/*     */       
/* 545 */       SchemaTreeNode newNode = new SchemaTreeNode(str, itemType.getLocator());
/*     */       
/* 547 */       this.currNode.add(newNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unionSimpleType(XSUnionSimpleType type) {
/* 555 */     int len = type.getMemberSize();
/* 556 */     StringBuffer ref = new StringBuffer();
/*     */     
/* 558 */     for (int i = 0; i < len; i++) {
/* 559 */       XSSimpleType member = type.getMember(i);
/* 560 */       if (member.isGlobal()) {
/* 561 */         ref.append(MessageFormat.format(" '{'{0}'}'{1}", new Object[] { member.getTargetNamespace(), member.getName() }));
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 568 */     String name = (ref.length() == 0) ? "Union" : ("Union memberTypes=\"" + ref + "\"");
/*     */     
/* 570 */     SchemaTreeNode newNode = new SchemaTreeNode(name, type.getLocator());
/* 571 */     this.currNode.add(newNode);
/* 572 */     this.currNode = newNode;
/*     */     
/* 574 */     for (int j = 0; j < len; j++) {
/* 575 */       XSSimpleType member = type.getMember(j);
/* 576 */       if (member.isLocal()) {
/* 577 */         simpleType(member);
/*     */       }
/*     */     } 
/* 580 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restrictionSimpleType(XSRestrictionSimpleType type) {
/* 588 */     if (type.getBaseType() == null) {
/*     */       
/* 590 */       if (!type.getName().equals("anySimpleType")) {
/* 591 */         throw new InternalError();
/*     */       }
/* 593 */       if (!"http://www.w3.org/2001/XMLSchema".equals(type.getTargetNamespace())) {
/* 594 */         throw new InternalError();
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 599 */     XSSimpleType baseType = type.getSimpleBaseType();
/*     */     
/* 601 */     String str = MessageFormat.format("Restriction {0}", new Object[] { baseType.isLocal() ? "" : (" base=\"{" + baseType.getTargetNamespace() + "}" + baseType.getName() + "\"") });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 606 */     SchemaTreeNode newNode = new SchemaTreeNode(str, baseType.getLocator());
/* 607 */     this.currNode.add(newNode);
/* 608 */     this.currNode = newNode;
/*     */     
/* 610 */     if (baseType.isLocal()) {
/* 611 */       simpleType(baseType);
/*     */     }
/*     */     
/* 614 */     Iterator<XSFacet> itr = type.iterateDeclaredFacets();
/* 615 */     while (itr.hasNext()) {
/* 616 */       facet(itr.next());
/*     */     }
/*     */     
/* 619 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void facet(XSFacet facet) {
/* 626 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("{0} value=\"{1}\"", new Object[] { facet.getName(), facet.getValue() }), facet.getLocator());
/*     */ 
/*     */ 
/*     */     
/* 630 */     this.currNode.add(newNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void notation(XSNotation notation) {
/* 637 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Notation name='\"0}\" public =\"{1}\" system=\"{2}\"", new Object[] { notation.getName(), notation.getPublicId(), notation.getSystemId() }), notation.getLocator());
/*     */ 
/*     */ 
/*     */     
/* 641 */     this.currNode.add(newNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType type) {
/* 648 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("ComplexType {0}", new Object[] { type.isLocal() ? "" : (" name=\"" + type.getName() + "\"") }), type.getLocator());
/*     */ 
/*     */ 
/*     */     
/* 652 */     this.currNode.add(newNode);
/* 653 */     this.currNode = newNode;
/*     */ 
/*     */ 
/*     */     
/* 657 */     if (type.getContentType().asSimpleType() != null) {
/*     */       
/* 659 */       SchemaTreeNode newNode2 = new SchemaTreeNode("Simple content", type.getContentType().getLocator());
/*     */       
/* 661 */       this.currNode.add(newNode2);
/* 662 */       this.currNode = newNode2;
/*     */       
/* 664 */       XSType baseType = type.getBaseType();
/*     */       
/* 666 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 668 */         String str = MessageFormat.format("Restriction base=\"<{0}>{1}\"", new Object[] { baseType.getTargetNamespace(), baseType.getName() });
/*     */ 
/*     */ 
/*     */         
/* 672 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/*     */         
/* 674 */         this.currNode.add(newNode3);
/* 675 */         this.currNode = newNode3;
/*     */         
/* 677 */         dumpComplexTypeAttribute(type);
/*     */         
/* 679 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */       else {
/*     */         
/* 683 */         String str = MessageFormat.format("Extension base=\"<{0}>{1}\"", new Object[] { baseType.getTargetNamespace(), baseType.getName() });
/*     */ 
/*     */ 
/*     */         
/* 687 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/*     */         
/* 689 */         this.currNode.add(newNode3);
/* 690 */         this.currNode = newNode3;
/*     */ 
/*     */         
/* 693 */         if (type.getTargetNamespace().compareTo(baseType.getTargetNamespace()) == 0 && type.getName().compareTo(baseType.getName()) == 0) {
/*     */ 
/*     */ 
/*     */           
/* 697 */           SchemaTreeNode newNodeRedefine = new SchemaTreeNode("redefine", type.getLocator());
/*     */ 
/*     */           
/* 700 */           this.currNode.add(newNodeRedefine);
/* 701 */           this.currNode = newNodeRedefine;
/* 702 */           baseType.visit(this);
/* 703 */           this.currNode = (SchemaTreeNode)newNodeRedefine.getParent();
/*     */         } 
/*     */ 
/*     */         
/* 707 */         dumpComplexTypeAttribute(type);
/*     */         
/* 709 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       } 
/*     */       
/* 712 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */     }
/*     */     else {
/*     */       
/* 716 */       SchemaTreeNode newNode2 = new SchemaTreeNode("Complex content", type.getContentType().getLocator());
/*     */       
/* 718 */       this.currNode.add(newNode2);
/* 719 */       this.currNode = newNode2;
/*     */       
/* 721 */       XSComplexType baseType = type.getBaseType().asComplexType();
/*     */       
/* 723 */       if (type.getDerivationMethod() == 2) {
/*     */         
/* 725 */         String str = MessageFormat.format("Restriction base=\"<{0}>{1}\"", new Object[] { baseType.getTargetNamespace(), baseType.getName() });
/*     */ 
/*     */ 
/*     */         
/* 729 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/*     */         
/* 731 */         this.currNode.add(newNode3);
/* 732 */         this.currNode = newNode3;
/*     */         
/* 734 */         type.getContentType().visit(this);
/* 735 */         dumpComplexTypeAttribute(type);
/*     */         
/* 737 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       }
/*     */       else {
/*     */         
/* 741 */         String str = MessageFormat.format("Extension base=\"'{'{0}'}'{1}\"", new Object[] { baseType.getTargetNamespace(), baseType.getName() });
/*     */ 
/*     */ 
/*     */         
/* 745 */         SchemaTreeNode newNode3 = new SchemaTreeNode(str, baseType.getLocator());
/*     */         
/* 747 */         this.currNode.add(newNode3);
/* 748 */         this.currNode = newNode3;
/*     */ 
/*     */         
/* 751 */         if (type.getTargetNamespace().compareTo(baseType.getTargetNamespace()) == 0 && type.getName().compareTo(baseType.getName()) == 0) {
/*     */ 
/*     */ 
/*     */           
/* 755 */           SchemaTreeNode newNodeRedefine = new SchemaTreeNode("redefine", type.getLocator());
/*     */ 
/*     */           
/* 758 */           this.currNode.add(newNodeRedefine);
/* 759 */           this.currNode = newNodeRedefine;
/* 760 */           baseType.visit(this);
/* 761 */           this.currNode = (SchemaTreeNode)newNodeRedefine.getParent();
/*     */         } 
/*     */ 
/*     */         
/* 765 */         type.getExplicitContent().visit(this);
/* 766 */         dumpComplexTypeAttribute(type);
/*     */         
/* 768 */         this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */       } 
/*     */       
/* 771 */       this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */     } 
/*     */     
/* 774 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void dumpComplexTypeAttribute(XSComplexType type) {
/* 785 */     Iterator<XSAttGroupDecl> itr = type.iterateAttGroups();
/* 786 */     while (itr.hasNext()) {
/* 787 */       dumpRef(itr.next());
/*     */     }
/*     */     
/* 790 */     itr = type.iterateDeclaredAttributeUses();
/* 791 */     while (itr.hasNext()) {
/* 792 */       attributeUse((XSAttributeUse)itr.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl decl) {
/* 800 */     elementDecl(decl, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void elementDecl(XSElementDecl decl, String extraAtts) {
/* 810 */     XSType type = decl.getType();
/*     */ 
/*     */ 
/*     */     
/* 814 */     String str = MessageFormat.format("Element name=\"{0}\"{1}{2}", new Object[] { decl.getName(), type.isLocal() ? "" : (" type=\"{" + type.getTargetNamespace() + "}" + type.getName() + "\""), extraAtts });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 821 */     SchemaTreeNode newNode = new SchemaTreeNode(str, decl.getLocator());
/* 822 */     this.currNode.add(newNode);
/* 823 */     this.currNode = newNode;
/*     */     
/* 825 */     if (type.isLocal() && 
/* 826 */       type.isLocal()) {
/* 827 */       type.visit(this);
/*     */     }
/*     */ 
/*     */     
/* 831 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 838 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Group name=\"{0}\"", new Object[] { decl.getName() }), decl.getLocator());
/*     */ 
/*     */     
/* 841 */     this.currNode.add(newNode);
/* 842 */     this.currNode = newNode;
/*     */     
/* 844 */     modelGroup(decl.getModelGroup());
/*     */     
/* 846 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 853 */     modelGroup(group, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void modelGroup(XSModelGroup group, String extraAtts) {
/* 863 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("{0}{1}", new Object[] { group.getCompositor(), extraAtts }), group.getLocator());
/*     */ 
/*     */     
/* 866 */     this.currNode.add(newNode);
/* 867 */     this.currNode = newNode;
/*     */     
/* 869 */     int len = group.getSize();
/* 870 */     for (int i = 0; i < len; i++) {
/* 871 */       particle(group.getChild(i));
/*     */     }
/*     */     
/* 874 */     this.currNode = (SchemaTreeNode)this.currNode.getParent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void particle(XSParticle part) {
/* 883 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 885 */     int i = part.getMaxOccurs();
/* 886 */     if (i == -1) {
/* 887 */       buf.append(" maxOccurs=\"unbounded\"");
/*     */     
/*     */     }
/* 890 */     else if (i != 1) {
/* 891 */       buf.append(" maxOccurs=\"" + i + "\"");
/*     */     } 
/*     */ 
/*     */     
/* 895 */     i = part.getMinOccurs();
/* 896 */     if (i != 1) {
/* 897 */       buf.append(" minOccurs=\"" + i + "\"");
/*     */     }
/*     */     
/* 900 */     final String extraAtts = buf.toString();
/*     */     
/* 902 */     part.getTerm().visit(new XSTermVisitor() {
/*     */           public void elementDecl(XSElementDecl decl) {
/* 904 */             if (decl.isLocal()) {
/* 905 */               SchemaTreeTraverser.this.elementDecl(decl, extraAtts);
/*     */             }
/*     */             else {
/*     */               
/* 909 */               SchemaTreeTraverser.SchemaTreeNode newNode = new SchemaTreeTraverser.SchemaTreeNode(MessageFormat.format("Element ref=\"'{'{0}'}'{1}\"{2}", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }), decl.getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 914 */               SchemaTreeTraverser.this.currNode.add(newNode);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void modelGroupDecl(XSModelGroupDecl decl) {
/* 920 */             SchemaTreeTraverser.SchemaTreeNode newNode = new SchemaTreeTraverser.SchemaTreeNode(MessageFormat.format("Group ref=\"'{'{0}'}'{1}\"{2}", new Object[] { decl.getTargetNamespace(), decl.getName(), this.val$extraAtts }), decl.getLocator());
/*     */ 
/*     */ 
/*     */             
/* 924 */             SchemaTreeTraverser.this.currNode.add(newNode);
/*     */           }
/*     */           
/*     */           public void modelGroup(XSModelGroup group) {
/* 928 */             SchemaTreeTraverser.this.modelGroup(group, extraAtts);
/*     */           }
/*     */           
/*     */           public void wildcard(XSWildcard wc) {
/* 932 */             SchemaTreeTraverser.this.wildcard(wc, extraAtts);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard wc) {
/* 941 */     wildcard(wc, "");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void wildcard(XSWildcard wc, String extraAtts) {
/* 952 */     SchemaTreeNode newNode = new SchemaTreeNode(MessageFormat.format("Any ", new Object[] { extraAtts }), wc.getLocator());
/*     */     
/* 954 */     this.currNode.add(newNode);
/*     */   }
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */   
/*     */   public void empty(XSContentType t) {}
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint ic) {}
/*     */   
/*     */   public void xpath(XSXPath xp) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\imp\\util\SchemaTreeTraverser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
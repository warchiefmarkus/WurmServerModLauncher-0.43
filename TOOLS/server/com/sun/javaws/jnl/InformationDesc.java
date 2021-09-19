/*     */ package com.sun.javaws.jnl;
/*     */ 
/*     */ import com.sun.deploy.xml.XMLAttribute;
/*     */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*     */ import com.sun.deploy.xml.XMLNode;
/*     */ import com.sun.deploy.xml.XMLNodeBuilder;
/*     */ import com.sun.deploy.xml.XMLable;
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InformationDesc
/*     */   implements XMLable
/*     */ {
/*     */   private String _title;
/*     */   private String _vendor;
/*     */   private URL _home;
/*     */   private String[] _descriptions;
/*     */   private IconDesc[] _icons;
/*     */   private ShortcutDesc _shortcutHints;
/*     */   private AssociationDesc[] _associations;
/*     */   private RContentDesc[] _relatedContent;
/*     */   private boolean _supportOfflineOperation;
/*     */   public static final int DESC_DEFAULT = 0;
/*     */   public static final int DESC_SHORT = 1;
/*     */   public static final int DESC_ONELINE = 2;
/*     */   public static final int DESC_TOOLTIP = 3;
/*     */   public static final int NOF_DESC = 4;
/*     */   public static final int ICON_SIZE_SMALL = 0;
/*     */   public static final int ICON_SIZE_MEDIUM = 1;
/*     */   public static final int ICON_SIZE_LARGE = 2;
/*     */   
/*     */   public InformationDesc(String paramString1, String paramString2, URL paramURL, String[] paramArrayOfString, IconDesc[] paramArrayOfIconDesc, ShortcutDesc paramShortcutDesc, RContentDesc[] paramArrayOfRContentDesc, AssociationDesc[] paramArrayOfAssociationDesc, boolean paramBoolean) {
/*  36 */     this._title = paramString1;
/*  37 */     this._vendor = paramString2;
/*  38 */     this._home = paramURL;
/*  39 */     if (paramArrayOfString == null) paramArrayOfString = new String[4]; 
/*  40 */     this._descriptions = paramArrayOfString;
/*  41 */     this._icons = paramArrayOfIconDesc;
/*  42 */     this._shortcutHints = paramShortcutDesc;
/*  43 */     this._associations = paramArrayOfAssociationDesc;
/*  44 */     this._relatedContent = paramArrayOfRContentDesc;
/*  45 */     this._supportOfflineOperation = paramBoolean;
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
/*     */   public String getTitle() {
/*  61 */     return this._title;
/*  62 */   } public String getVendor() { return this._vendor; }
/*  63 */   public URL getHome() { return this._home; }
/*  64 */   public boolean supportsOfflineOperation() { return this._supportOfflineOperation; }
/*  65 */   public IconDesc[] getIcons() { return this._icons; }
/*  66 */   public ShortcutDesc getShortcut() { return this._shortcutHints; }
/*  67 */   public AssociationDesc[] getAssociations() { return this._associations; } public RContentDesc[] getRelatedContent() {
/*  68 */     return this._relatedContent;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription(int paramInt) {
/*  74 */     return this._descriptions[paramInt];
/*     */   }
/*     */ 
/*     */   
/*     */   public IconDesc getIconLocation(int paramInt1, int paramInt2) {
/*  79 */     byte b1 = 0;
/*  80 */     byte b2 = 0;
/*  81 */     switch (paramInt1) { case 0:
/*  82 */         b1 = b2 = 16; break;
/*  83 */       case 1: b1 = b2 = 32; break;
/*  84 */       case 2: b1 = b2 = 64;
/*     */         break; }
/*     */     
/*  87 */     IconDesc iconDesc = null;
/*  88 */     long l = 0L;
/*  89 */     for (byte b3 = 0; b3 < this._icons.length; b3++) {
/*  90 */       IconDesc iconDesc1 = this._icons[b3];
/*  91 */       if (iconDesc1.getKind() == paramInt2) {
/*     */         
/*  93 */         if (iconDesc1.getHeight() == b1 && iconDesc1.getWidth() == b2) {
/*  94 */           return iconDesc1;
/*     */         }
/*     */         
/*  97 */         if (iconDesc1.getHeight() == 0 && iconDesc1.getWidth() == 0) {
/*     */           
/*  99 */           if (iconDesc == null) {
/* 100 */             iconDesc = iconDesc1;
/*     */           }
/*     */         } else {
/*     */           
/* 104 */           long l1 = Math.abs(iconDesc1.getHeight() * iconDesc1.getWidth() - b1 * b2);
/* 105 */           if (l == 0L || l1 < l) {
/* 106 */             l = l1;
/* 107 */             iconDesc = iconDesc1;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 112 */     return iconDesc;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLNode asXML() {
/* 117 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/*     */     
/* 119 */     XMLNodeBuilder xMLNodeBuilder = new XMLNodeBuilder("information", xMLAttributeBuilder.getAttributeList());
/* 120 */     xMLNodeBuilder.add("title", this._title);
/* 121 */     xMLNodeBuilder.add("vendor", this._vendor);
/* 122 */     xMLNodeBuilder.add(new XMLNode("homepage", new XMLAttribute("href", (this._home != null) ? this._home.toString() : null), null, null));
/*     */ 
/*     */ 
/*     */     
/* 126 */     xMLNodeBuilder.add(getDescriptionNode(0, ""));
/* 127 */     xMLNodeBuilder.add(getDescriptionNode(1, "short"));
/* 128 */     xMLNodeBuilder.add(getDescriptionNode(2, "one-line"));
/* 129 */     xMLNodeBuilder.add(getDescriptionNode(3, "tooltip"));
/* 130 */     if (this._icons != null) {
/* 131 */       for (byte b = 0; b < this._icons.length; b++) {
/* 132 */         xMLNodeBuilder.add(this._icons[b]);
/*     */       }
/*     */     }
/*     */     
/* 136 */     if (this._shortcutHints != null) {
/* 137 */       xMLNodeBuilder.add(this._shortcutHints);
/*     */     }
/*     */     
/* 140 */     if (this._associations != null) {
/* 141 */       for (byte b = 0; b < this._associations.length; b++) {
/* 142 */         xMLNodeBuilder.add(this._associations[b]);
/*     */       }
/*     */     }
/*     */     
/* 146 */     if (this._relatedContent != null) {
/* 147 */       for (byte b = 0; b < this._relatedContent.length; b++) {
/* 148 */         xMLNodeBuilder.add(this._relatedContent[b]);
/*     */       }
/*     */     }
/*     */     
/* 152 */     if (this._supportOfflineOperation) {
/* 153 */       xMLNodeBuilder.add(new XMLNode("offline-allowed", null));
/*     */     }
/* 155 */     return xMLNodeBuilder.getNode();
/*     */   }
/*     */   
/*     */   private XMLNode getDescriptionNode(int paramInt, String paramString) {
/* 159 */     String str = this._descriptions[paramInt];
/* 160 */     if (str == null) return null; 
/* 161 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 162 */     xMLAttributeBuilder.add("kind", paramString);
/* 163 */     return new XMLNode("description", xMLAttributeBuilder.getAttributeList(), new XMLNode(str), null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\InformationDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
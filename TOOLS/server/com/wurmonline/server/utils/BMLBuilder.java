/*      */ package com.wurmonline.server.utils;
/*      */ 
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import java.awt.Color;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
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
/*      */ public class BMLBuilder
/*      */ {
/*      */   private StringBuilder sb;
/*      */   
/*      */   public static BMLBuilder createBMLUpdate(String... updates) {
/*   28 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*   30 */     builder.openBracket("update");
/*      */     
/*   32 */     for (String update : updates) {
/*   33 */       builder.addString(update);
/*      */     }
/*   35 */     builder.closeBracket();
/*      */     
/*   37 */     return builder;
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
/*      */   public static BMLBuilder createBMLBorderPanel(@Nullable BMLBuilder north, @Nullable BMLBuilder west, @Nullable BMLBuilder center, @Nullable BMLBuilder east, @Nullable BMLBuilder south) {
/*   53 */     return createBMLBorderPanel(north, west, center, east, south, 0, 0);
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
/*      */   public static BMLBuilder createBMLBorderPanel(@Nullable BMLBuilder north, @Nullable BMLBuilder west, @Nullable BMLBuilder center, @Nullable BMLBuilder east, @Nullable BMLBuilder south, int width, int height) {
/*   71 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*   73 */     builder.openBracket("border");
/*      */     
/*   75 */     if (width > 0 || height > 0)
/*   76 */       builder.addString("size=\"" + width + "," + height + "\";"); 
/*   77 */     if (north != null) {
/*   78 */       builder.addString(north.toString());
/*      */     } else {
/*   80 */       builder.addString("null;");
/*      */     } 
/*   82 */     if (west != null) {
/*   83 */       builder.addString(west.toString());
/*      */     } else {
/*   85 */       builder.addString("null;");
/*      */     } 
/*   87 */     if (center != null) {
/*   88 */       builder.addString(center.toString());
/*      */     } else {
/*   90 */       builder.addString("null;");
/*      */     } 
/*   92 */     if (east != null) {
/*   93 */       builder.addString(east.toString());
/*      */     } else {
/*   95 */       builder.addString("null;");
/*      */     } 
/*   97 */     if (south != null) {
/*   98 */       builder.addString(south.toString());
/*      */     } else {
/*  100 */       builder.addString("null;");
/*      */     } 
/*  102 */     builder.closeBracket();
/*      */     
/*  104 */     return builder;
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
/*      */   public static BMLBuilder createNormalWindow(String id, String question, BMLBuilder content) {
/*  117 */     BMLBuilder header = createCenteredNode(
/*  118 */         createGenericBuilder().addText(question, null, TextType.BOLD, null));
/*      */     
/*  120 */     BMLBuilder center = createScrollPanelNode(true, false);
/*  121 */     center.addString(
/*  122 */         createVertArrayNode(true)
/*  123 */         .addPassthrough("id", id)
/*  124 */         .addString(content.toString())
/*  125 */         .toString());
/*      */ 
/*      */     
/*  128 */     return createBMLBorderPanel(header, null, center, null, null);
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
/*      */   public static BMLBuilder createNoQuestionWindow(String id, BMLBuilder content) {
/*  140 */     BMLBuilder center = createScrollPanelNode(true, false);
/*  141 */     center.addString(
/*  142 */         createVertArrayNode(true)
/*  143 */         .addPassthrough("id", id)
/*  144 */         .addString(content.toString())
/*  145 */         .toString());
/*      */ 
/*      */     
/*  148 */     return createBMLBorderPanel(null, null, center, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createCenteredNode(BMLBuilder content) {
/*  159 */     return createAlignedNode("center", content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createLeftAlignedNode(BMLBuilder content) {
/*  170 */     return createAlignedNode("left", content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createRightAlignedNode(BMLBuilder content) {
/*  181 */     return createAlignedNode("right", content);
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
/*      */   public static BMLBuilder createAlignedNode(String alignment, BMLBuilder content) {
/*  193 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  195 */     builder.openBracket(alignment);
/*      */     
/*  197 */     builder.addString(content.toString());
/*      */     
/*  199 */     builder.closeBracket();
/*      */     
/*  201 */     return builder;
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
/*      */   public static BMLBuilder createScrollPanelNode(boolean vertical, boolean horizontal) {
/*  213 */     return createScrollPanelNode(vertical, horizontal, 0, 0);
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
/*      */   public static BMLBuilder createScrollPanelNode(boolean vertical, boolean horizontal, int width, int height) {
/*  227 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  229 */     builder.openBracket("scroll");
/*  230 */     if (width > 0 || height > 0)
/*  231 */       builder.addString("size=\"" + width + "," + height + "\";"); 
/*  232 */     builder.addString("vertical=\"" + vertical + "\";");
/*  233 */     builder.addString("horizontal=\"" + horizontal + "\";");
/*      */     
/*  235 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createVertArrayNode(boolean rescale) {
/*  246 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  248 */     builder.openBracket("varray");
/*  249 */     builder.addString("rescale=\"" + rescale + "\";");
/*      */     
/*  251 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createHorizArrayNode(boolean rescale) {
/*  262 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  264 */     builder.openBracket("harray");
/*  265 */     builder.addString("rescale=\"" + rescale + "\";");
/*      */     
/*  267 */     return builder;
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
/*      */   public static BMLBuilder createTable(int columns) {
/*  280 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  282 */     builder.openBracket("table");
/*  283 */     builder.addString("cols=\"" + columns + "\";");
/*      */     
/*  285 */     return builder;
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
/*      */   public static BMLBuilder createTreeList(String id, int columnCount, int height, boolean showHeader) {
/*  299 */     BMLBuilder builder = new BMLBuilder();
/*      */     
/*  301 */     builder.openBracket("tree");
/*      */     
/*  303 */     builder.addString("id=\"" + id + "\";");
/*  304 */     builder.addString("height=\"" + height + "\";");
/*  305 */     builder.addString("cols=\"" + columnCount + "\";");
/*  306 */     builder.addString("showheader=\"" + showHeader + "\";");
/*      */ 
/*      */     
/*  309 */     return builder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BMLBuilder createGenericBuilder() {
/*  319 */     return new BMLBuilder();
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
/*      */   public static String createHeader(String text) {
/*  331 */     return createGenericBuilder().addHeader(text).toString();
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
/*      */   public static String createLabel(String text) {
/*  343 */     return createGenericBuilder().addLabel(text).toString();
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
/*      */   public static String createButton(String id, String text) {
/*  356 */     return createButton(id, text, true);
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
/*      */   public static String createButton(String id, String text, boolean enabled) {
/*  370 */     return createGenericBuilder().addButton(id, text, enabled).toString();
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
/*      */   public static String createButton(String id, String text, int width, int height, boolean enabled) {
/*  384 */     return createGenericBuilder().addButton(id, text, width, height, enabled).toString();
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
/*      */   public static String createInput(String id, @Nullable String text) {
/*  397 */     return createGenericBuilder().addInput(id, text, 0, 0).toString();
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
/*      */   public static String createText(String text) {
/*  409 */     return createGenericBuilder().addText(text).toString();
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
/*      */   public static String createCheckbox(String id, String text, boolean selected) {
/*  423 */     return createGenericBuilder().addCheckbox(id, text, selected).toString();
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
/*      */   public static String createDropdown(String id, String defaultOption, String... options) {
/*  437 */     return createGenericBuilder().addDropdown(id, defaultOption, options).toString();
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
/*      */   public static String createRadioButton(String id, String group, String text, boolean selected) {
/*  452 */     return createGenericBuilder().addRadioButton(id, group, text, selected).toString();
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
/*      */   public static String createImage(String imageSrc, int width, int height) {
/*  466 */     return createGenericBuilder().addImage(imageSrc, width, height).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TreeListRowData createTLRD(String text) {
/*  477 */     return new TreeListRowData(text);
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
/*      */   public static TreeListRowData createTLRD(String id, String text, boolean checkbox, boolean selected) {
/*  491 */     return new TreeListRowData(id, text, checkbox, selected);
/*      */   }
/*      */   
/*      */   public enum TextType
/*      */   {
/*  496 */     NONE(""),
/*  497 */     BOLD("bold"),
/*  498 */     ITALIC("italic"),
/*  499 */     BOLDITALIC("bolditalic");
/*      */     
/*      */     private final String typeString;
/*      */ 
/*      */     
/*      */     TextType(String typeString) {
/*  505 */       this.typeString = typeString;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*  511 */   private int openBrackets = 0;
/*      */ 
/*      */   
/*      */   private BMLBuilder() {
/*  515 */     this.sb = new StringBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BMLBuilder addHeader(String text) {
/*  526 */     return addHeader(text, null);
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
/*      */   public BMLBuilder addHeader(String text, @Nullable Color color) {
/*  538 */     openBracket("header");
/*      */     
/*  540 */     if (color != null)
/*  541 */       addColor("color", color); 
/*  542 */     this.sb.append("text=\"" + text + "\";");
/*      */     
/*  544 */     closeBracket();
/*      */     
/*  546 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BMLBuilder addText(String text) {
/*  557 */     return addText(text, null, null, null);
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
/*      */   public BMLBuilder addText(String text, @Nullable String hover, @Nullable TextType type, @Nullable Color color) {
/*  571 */     return addText(text, hover, type, color, 0, 0);
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
/*      */   public BMLBuilder addText(String text, @Nullable String hover, @Nullable TextType type, @Nullable Color color, int width, int height) {
/*  588 */     openBracket("text");
/*      */     
/*  590 */     if (width > 0 || height > 0)
/*  591 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  592 */     if (type != null && type != TextType.NONE)
/*  593 */       this.sb.append("type=\"" + type.typeString + "\";"); 
/*  594 */     if (hover != null)
/*  595 */       this.sb.append("hover=\"" + hover + "\";"); 
/*  596 */     if (color != null)
/*  597 */       addColor("color", color); 
/*  598 */     this.sb.append("text=\"" + text + "\";");
/*      */     
/*  600 */     closeBracket();
/*      */     
/*  602 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BMLBuilder addLabel(String text) {
/*  613 */     return addLabel(text, null, null, null);
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
/*      */   public BMLBuilder addLabel(String text, @Nullable String hover, @Nullable TextType type, @Nullable Color color) {
/*  627 */     return addLabel(text, hover, type, color, 0, 0);
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
/*      */   public BMLBuilder addLabel(String text, @Nullable String hover, @Nullable TextType type, @Nullable Color color, int width, int height) {
/*  644 */     openBracket("label");
/*      */     
/*  646 */     if (width > 0 || height > 0)
/*  647 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  648 */     if (type != null && type != TextType.NONE)
/*  649 */       this.sb.append("type=\"" + type.typeString + "\";"); 
/*  650 */     if (hover != null)
/*  651 */       this.sb.append("hover=\"" + hover + "\";"); 
/*  652 */     if (color != null)
/*  653 */       addColor("color", color); 
/*  654 */     this.sb.append("text=\"" + text + "\";");
/*      */     
/*  656 */     closeBracket();
/*      */     
/*  658 */     return this;
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
/*      */   public BMLBuilder addInput(String id, @Nullable String text, int maxChars, int maxLines) {
/*  672 */     return addInput(id, null, true, text, maxChars, maxLines, null, null, null);
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
/*      */   public BMLBuilder addInput(String id, @Nullable String onEnter, boolean enabled, @Nullable String text, int maxChars, int maxLines, @Nullable Color color, @Nullable Color bgColor, @Nullable String bgTexture) {
/*  692 */     return addInput(id, onEnter, enabled, text, maxChars, maxLines, color, bgColor, bgTexture, 0, 0);
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
/*      */   public BMLBuilder addInput(String id, @Nullable String onEnter, boolean enabled, @Nullable String text, int maxChars, int maxLines, @Nullable Color color, @Nullable Color bgColor, @Nullable String bgTexture, int width, int height) {
/*  714 */     openBracket("input");
/*      */     
/*  716 */     if (width > 0 || height > 0)
/*  717 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  718 */     this.sb.append("id=\"" + id + "\";");
/*  719 */     if (onEnter != null)
/*  720 */       this.sb.append("onenter=\"" + onEnter + "\";"); 
/*  721 */     this.sb.append("enabled=\"" + enabled + "\";");
/*  722 */     if (text != null)
/*  723 */       this.sb.append("text=\"" + text + "\";"); 
/*  724 */     if (maxChars > 0)
/*  725 */       this.sb.append("maxchars=\"" + maxChars + "\";"); 
/*  726 */     if (maxLines > 0)
/*  727 */       this.sb.append("maxlines=\"" + maxLines + "\";"); 
/*  728 */     if (color != null)
/*  729 */       addColor("color", color); 
/*  730 */     if (bgColor != null)
/*  731 */       addColor("bgcolor", bgColor); 
/*  732 */     if (bgTexture != null) {
/*  733 */       this.sb.append("bgtexture=\"" + bgTexture + "\";");
/*      */     }
/*  735 */     closeBracket();
/*      */     
/*  737 */     return this;
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
/*      */   public BMLBuilder addPassthrough(String id, String text) {
/*  749 */     openBracket("passthrough");
/*      */     
/*  751 */     this.sb.append("id=\"" + id + "\";");
/*  752 */     this.sb.append("text=\"" + text + "\";");
/*      */     
/*  754 */     closeBracket();
/*      */     
/*  756 */     return this;
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
/*      */   public BMLBuilder addDropdown(String id, String defaultOption, String... options) {
/*  769 */     return addDropdown(id, defaultOption, 0, 0, options);
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
/*      */   public BMLBuilder addDropdown(String id, String defaultOption, int width, int height, String... options) {
/*  784 */     openBracket("dropdown");
/*      */     
/*  786 */     if (width > 0 || height > 0)
/*  787 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  788 */     this.sb.append("id=\"" + id + "\";");
/*  789 */     this.sb.append("default=\"" + defaultOption + "\";");
/*      */     
/*  791 */     this.sb.append("options=\"");
/*      */     
/*  793 */     int count = 0;
/*  794 */     for (String s : options) {
/*      */       
/*  796 */       this.sb.append(s);
/*  797 */       if (++count < options.length)
/*  798 */         this.sb.append(","); 
/*      */     } 
/*  800 */     this.sb.append("\";");
/*      */ 
/*      */     
/*  803 */     closeBracket();
/*      */     
/*  805 */     return this;
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
/*      */   public BMLBuilder addButton(String id, String text) {
/*  817 */     return addButton(id, text, null, null, null, false);
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
/*      */   public BMLBuilder addButton(String id, String text, boolean enabled) {
/*  829 */     return addButton(id, text, null, null, null, false, 0, 0, enabled);
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
/*      */   public BMLBuilder addButton(String id, String text, int width, int height, boolean enabled) {
/*  841 */     return addButton(id, text, null, null, null, false, width, height, enabled);
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
/*      */   public BMLBuilder addButton(String id, String text, @Nullable String confirmQuestion, @Nullable String confirmString, @Nullable String hover, boolean isDefaultButton) {
/*  858 */     return addButton(id, text, confirmQuestion, confirmString, hover, isDefaultButton, 0, 0, true);
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
/*      */   public BMLBuilder addButton(String id, String text, @Nullable String confirmQuestion, @Nullable String confirmString, @Nullable String hover, boolean isDefaultButton, int width, int height, boolean enabled) {
/*  877 */     openBracket("button");
/*      */     
/*  879 */     if (width > 0 || height > 0)
/*  880 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  881 */     this.sb.append("id=\"" + id + "\";");
/*  882 */     this.sb.append("text=\"" + text + "\";");
/*  883 */     if (confirmQuestion != null)
/*  884 */       this.sb.append("question=\"" + confirmQuestion + "\";"); 
/*  885 */     if (confirmString != null)
/*  886 */       this.sb.append("confirm=\"" + confirmString + "\";"); 
/*  887 */     if (hover != null)
/*  888 */       this.sb.append("hover=\"" + hover + "\";"); 
/*  889 */     this.sb.append("default=\"" + isDefaultButton + "\";");
/*  890 */     this.sb.append("enabled=\"" + enabled + "\";");
/*      */     
/*  892 */     closeBracket();
/*      */     
/*  894 */     return this;
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
/*      */   public BMLBuilder addRadioButton(String id, String group, String text, boolean selected) {
/*  908 */     return addRadioButton(id, group, text, null, selected, true, false);
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
/*      */   public BMLBuilder addRadioButton(String id, String group, String text, @Nullable String hover, boolean selected, boolean enabled, boolean hidden) {
/*  926 */     return addRadioButton(id, group, text, hover, selected, enabled, hidden, 0, 0);
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
/*      */   public BMLBuilder addRadioButton(String id, String group, String text, @Nullable String hover, boolean selected, boolean enabled, boolean hidden, int width, int height) {
/*  946 */     openBracket("radio");
/*      */     
/*  948 */     if (width > 0 || height > 0)
/*  949 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/*  950 */     this.sb.append("id=\"" + id + "\";");
/*  951 */     this.sb.append("group=\"" + group + "\";");
/*  952 */     this.sb.append("text=\"" + text + "\";");
/*  953 */     if (hover != null)
/*  954 */       this.sb.append("hover=\"" + hover + "\";"); 
/*  955 */     this.sb.append("selected=\"" + selected + "\";");
/*  956 */     this.sb.append("enabled=\"" + enabled + "\";");
/*  957 */     this.sb.append("hidden=\"" + hidden + "\";");
/*      */     
/*  959 */     closeBracket();
/*      */     
/*  961 */     return this;
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
/*      */   public BMLBuilder addCheckbox(String id, String text, boolean selected) {
/*  974 */     return addCheckbox(id, text, null, null, null, null, null, selected, true, null);
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
/*      */   public BMLBuilder addCheckbox(String id, String text, @Nullable String confirmQuestion, @Nullable String confirmString, @Nullable String unconfirmQuestion, @Nullable String unconfirmString, @Nullable String hover, boolean selected, boolean enabled, @Nullable Color color) {
/*  996 */     return addCheckbox(id, text, confirmQuestion, confirmString, unconfirmQuestion, unconfirmString, hover, selected, enabled, color, 0, 0);
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
/*      */   public BMLBuilder addCheckbox(String id, String text, @Nullable String confirmQuestion, @Nullable String confirmString, @Nullable String unconfirmQuestion, @Nullable String unconfirmString, @Nullable String hover, boolean selected, boolean enabled, @Nullable Color color, int width, int height) {
/* 1020 */     openBracket("checkbox");
/*      */     
/* 1022 */     if (width > 0 || height > 0)
/* 1023 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/* 1024 */     this.sb.append("id=\"" + id + "\";");
/* 1025 */     this.sb.append("text=\"" + text + "\";");
/* 1026 */     if (confirmQuestion != null)
/* 1027 */       this.sb.append("question=\"" + confirmQuestion + "\";"); 
/* 1028 */     if (confirmString != null)
/* 1029 */       this.sb.append("confirm=\"" + confirmString + "\";"); 
/* 1030 */     if (unconfirmQuestion != null)
/* 1031 */       this.sb.append("unquestion=\"" + unconfirmQuestion + "\";"); 
/* 1032 */     if (unconfirmString != null)
/* 1033 */       this.sb.append("unconfirm=\"" + unconfirmString + "\";"); 
/* 1034 */     if (hover != null)
/* 1035 */       this.sb.append("hover=\"" + hover + "\";"); 
/* 1036 */     this.sb.append("selected=\"" + selected + "\";");
/* 1037 */     this.sb.append("enabled=\"" + enabled + "\";");
/* 1038 */     if (color != null) {
/* 1039 */       addColor("color", color);
/*      */     }
/* 1041 */     closeBracket();
/*      */     
/* 1043 */     return this;
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
/*      */   public BMLBuilder addImage(String imageSrc, int width, int height) {
/* 1056 */     return addImage(imageSrc, null, width, height);
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
/*      */   public BMLBuilder addImage(String imageSrc, @Nullable String hoverText, int width, int height) {
/* 1070 */     openBracket("image");
/*      */     
/* 1072 */     if (width > 0 || height > 0)
/* 1073 */       this.sb.append("size=\"" + width + "," + height + "\";"); 
/* 1074 */     this.sb.append("src=\"" + imageSrc + "\";");
/* 1075 */     if (hoverText != null) {
/* 1076 */       this.sb.append("text=\"" + hoverText + "\";");
/*      */     }
/* 1078 */     closeBracket();
/*      */     
/* 1080 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BMLBuilder addTreeListColumn(String text) {
/* 1091 */     return addTreeListColumn(text, 0);
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
/*      */   public BMLBuilder addTreeListColumn(String text, int width) {
/* 1103 */     openBracket("col");
/*      */     
/* 1105 */     this.sb.append("text=\"" + text + "\";");
/* 1106 */     if (width > 0) {
/* 1107 */       this.sb.append("width=\"" + width + "\";");
/*      */     }
/* 1109 */     closeBracket();
/*      */     
/* 1111 */     return this;
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
/*      */   public BMLBuilder addTreeListRow(String id, String name, TreeListRowData... colData) {
/* 1124 */     return addTreeListRow(id, name, null, 0, 0, colData);
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
/*      */   public BMLBuilder addTreeListRow(String id, String name, @Nullable String hover, int rarity, int children, TreeListRowData... colData) {
/* 1142 */     openBracket("row");
/*      */     
/* 1144 */     this.sb.append("id=\"" + id + "\";");
/* 1145 */     this.sb.append("name=\"" + name + "\";");
/* 1146 */     if (hover != null)
/* 1147 */       this.sb.append("hover=\"" + hover + "\";"); 
/* 1148 */     this.sb.append("rarity=\"" + rarity + "\";");
/* 1149 */     this.sb.append("children=\"" + children + "\";");
/*      */     
/* 1151 */     for (TreeListRowData col : colData) {
/*      */       
/* 1153 */       openBracket("col");
/*      */       
/* 1155 */       this.sb.append("text=\"" + col.text + "\";");
/* 1156 */       if (col.id != null)
/* 1157 */         this.sb.append("id=\"" + col.id + "\";"); 
/* 1158 */       if (col.checkbox)
/* 1159 */         this.sb.append("checkbox=\"" + col.checkbox + "\";"); 
/* 1160 */       if (col.selected) {
/* 1161 */         this.sb.append("selected=\"" + col.selected + "\";");
/*      */       }
/* 1163 */       closeBracket();
/*      */     } 
/*      */     
/* 1166 */     closeBracket();
/*      */     
/* 1168 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addColor(String colorType, @Nonnull Color color) {
/* 1179 */     this.sb.append(colorType + "=\"" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "\";");
/*      */   }
/*      */ 
/*      */   
/*      */   public void openBracket(String type) {
/* 1184 */     this.sb.append(type);
/* 1185 */     this.sb.append("{");
/*      */     
/* 1187 */     this.openBrackets++;
/*      */   }
/*      */ 
/*      */   
/*      */   public void closeBracket() {
/* 1192 */     this.sb.append("}");
/*      */     
/* 1194 */     this.openBrackets--;
/*      */   }
/*      */ 
/*      */   
/*      */   public BMLBuilder prependString(String s) {
/* 1199 */     this.sb.insert(0, s);
/*      */     
/* 1201 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public BMLBuilder addString(String s) {
/* 1206 */     this.sb.append(s);
/*      */     
/* 1208 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1213 */     for (int i = 0; i < this.openBrackets; i++) {
/* 1214 */       this.sb.append("}");
/*      */     }
/* 1216 */     return this.sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public BMLBuilder maybeAddSkipButton() {
/* 1221 */     return maybeAddSkipButton(null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public BMLBuilder maybeAddSkipButton(Player p, boolean close) {
/* 1226 */     if (!close) {
/*      */       
/* 1228 */       if (Servers.localServer.testServer || Server.getInstance().isPS()) {
/* 1229 */         return addText("", null, null, null, 35, 0)
/* 1230 */           .addButton("skip", "Skip Stage", " ", "Are you sure you want to skip this stage of the tutorial?", null, false, 80, 20, true);
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1235 */       if (p == null) {
/* 1236 */         return this;
/*      */       }
/* 1238 */       if (Servers.localServer.testServer || Servers.isThisAPvpServer() || p.hasFlag(42) || 
/* 1239 */         System.currentTimeMillis() - (p.getSaveFile()).creationDate > 14515200000L)
/*      */       {
/*      */         
/* 1242 */         return addText("", null, null, null, 35, 0)
/* 1243 */           .addButton("close", "Close Tutorial", " ", "Are you sure you want to skip the tutorial?", null, false, 80, 20, true);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1248 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class TreeListRowData
/*      */   {
/*      */     private String id;
/*      */ 
/*      */     
/*      */     private String text;
/*      */ 
/*      */     
/*      */     private boolean checkbox = false;
/*      */ 
/*      */     
/*      */     private boolean selected = false;
/*      */ 
/*      */ 
/*      */     
/*      */     public TreeListRowData(String text) {
/* 1269 */       this(null, text, false, false);
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
/*      */     public TreeListRowData(String id, String text, boolean checkbox, boolean selected) {
/* 1282 */       this.id = id;
/* 1283 */       this.text = text;
/* 1284 */       this.checkbox = checkbox;
/* 1285 */       this.selected = selected;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\BMLBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
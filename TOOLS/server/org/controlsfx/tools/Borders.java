/*     */ package org.controlsfx.tools;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.layout.BorderStroke;
/*     */ import javafx.scene.layout.BorderStrokeStyle;
/*     */ import javafx.scene.layout.BorderWidths;
/*     */ import javafx.scene.layout.CornerRadii;
/*     */ import javafx.scene.layout.StackPane;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
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
/*     */ public final class Borders
/*     */ {
/* 112 */   private static final Color DEFAULT_BORDER_COLOR = Color.DARKGRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Node node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final List<Border> borders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Borders wrap(Node n) {
/* 134 */     return new Borders(n);
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
/*     */   private Borders(Node n) {
/* 146 */     this.node = n;
/* 147 */     this.borders = new ArrayList<>();
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
/*     */   public EmptyBorders emptyBorder() {
/* 165 */     return new EmptyBorders(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EtchedBorders etchedBorder() {
/* 176 */     return new EtchedBorders(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineBorders lineBorder() {
/* 184 */     return new LineBorders(this);
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
/*     */   public Borders addBorder(Border border) {
/* 196 */     this.borders.add(border);
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node build() {
/* 206 */     Node bundle = this.node;
/* 207 */     for (int i = this.borders.size() - 1; i >= 0; i--) {
/* 208 */       Border border = this.borders.get(i);
/* 209 */       bundle = border.wrap(bundle);
/*     */     } 
/* 211 */     return bundle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class EmptyBorders
/*     */   {
/*     */     private final Borders parent;
/*     */ 
/*     */ 
/*     */     
/*     */     private double top;
/*     */ 
/*     */     
/*     */     private double right;
/*     */ 
/*     */     
/*     */     private double bottom;
/*     */ 
/*     */     
/*     */     private double left;
/*     */ 
/*     */ 
/*     */     
/*     */     private EmptyBorders(Borders parent) {
/* 237 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EmptyBorders padding(double padding) {
/* 245 */       return padding(padding, padding, padding, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EmptyBorders padding(double top, double right, double bottom, double left) {
/* 254 */       this.top = top;
/* 255 */       this.right = right;
/* 256 */       this.bottom = bottom;
/* 257 */       this.left = left;
/* 258 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Borders build() {
/* 267 */       this.parent.addBorder(new Borders.StrokeBorder(null, new BorderStroke[] { buildStroke() }));
/* 268 */       return this.parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node buildAll() {
/* 278 */       build();
/* 279 */       return this.parent.build();
/*     */     }
/*     */     
/*     */     private BorderStroke buildStroke() {
/* 283 */       return new BorderStroke(null, BorderStrokeStyle.NONE, null, new BorderWidths(this.top, this.right, this.bottom, this.left), Insets.EMPTY);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class EtchedBorders
/*     */   {
/*     */     private final Borders parent;
/*     */ 
/*     */ 
/*     */     
/*     */     private String title;
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean raised = false;
/*     */ 
/*     */     
/* 303 */     private double outerTopPadding = 10.0D;
/* 304 */     private double outerRightPadding = 10.0D;
/* 305 */     private double outerBottomPadding = 10.0D;
/* 306 */     private double outerLeftPadding = 10.0D;
/*     */     
/* 308 */     private double innerTopPadding = 15.0D;
/* 309 */     private double innerRightPadding = 15.0D;
/* 310 */     private double innerBottomPadding = 15.0D;
/* 311 */     private double innerLeftPadding = 15.0D;
/*     */     
/* 313 */     private double topLeftRadius = 0.0D;
/* 314 */     private double topRightRadius = 0.0D;
/* 315 */     private double bottomRightRadius = 0.0D;
/* 316 */     private double bottomLeftRadius = 0.0D;
/*     */     
/* 318 */     private Color highlightColor = Borders.DEFAULT_BORDER_COLOR;
/* 319 */     private Color shadowColor = Color.WHITE;
/*     */ 
/*     */     
/*     */     private EtchedBorders(Borders parent) {
/* 323 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders highlight(Color highlight) {
/* 330 */       this.highlightColor = highlight;
/* 331 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders shadow(Color shadow) {
/* 338 */       this.shadowColor = shadow;
/* 339 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders raised() {
/* 349 */       this.raised = true;
/* 350 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders title(String title) {
/* 357 */       this.title = title;
/* 358 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders outerPadding(double padding) {
/* 365 */       return outerPadding(padding, padding, padding, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders outerPadding(double topPadding, double rightPadding, double bottomPadding, double leftPadding) {
/* 374 */       this.outerTopPadding = topPadding;
/* 375 */       this.outerRightPadding = rightPadding;
/* 376 */       this.outerBottomPadding = bottomPadding;
/* 377 */       this.outerLeftPadding = leftPadding;
/*     */       
/* 379 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders innerPadding(double padding) {
/* 386 */       return innerPadding(padding, padding, padding, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders innerPadding(double topPadding, double rightPadding, double bottomPadding, double leftPadding) {
/* 395 */       this.innerTopPadding = topPadding;
/* 396 */       this.innerRightPadding = rightPadding;
/* 397 */       this.innerBottomPadding = bottomPadding;
/* 398 */       this.innerLeftPadding = leftPadding;
/*     */       
/* 400 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders radius(double radius) {
/* 407 */       return radius(radius, radius, radius, radius);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EtchedBorders radius(double topLeft, double topRight, double bottomRight, double bottomLeft) {
/* 416 */       this.topLeftRadius = topLeft;
/* 417 */       this.topRightRadius = topRight;
/* 418 */       this.bottomRightRadius = bottomRight;
/* 419 */       this.bottomLeftRadius = bottomLeft;
/* 420 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Borders build() {
/* 429 */       Color inner = this.raised ? this.shadowColor : this.highlightColor;
/* 430 */       Color outer = this.raised ? this.highlightColor : this.shadowColor;
/* 431 */       BorderStroke innerStroke = new BorderStroke((Paint)inner, BorderStrokeStyle.SOLID, new CornerRadii(this.topLeftRadius, this.topRightRadius, this.bottomRightRadius, this.bottomLeftRadius, false), new BorderWidths(1.0D));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 436 */       BorderStroke outerStroke = new BorderStroke((Paint)outer, BorderStrokeStyle.SOLID, new CornerRadii(this.topLeftRadius, this.topRightRadius, this.bottomRightRadius, this.bottomLeftRadius, false), new BorderWidths(1.0D), new Insets(1.0D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 445 */       BorderStroke outerPadding = (new Borders.EmptyBorders(this.parent)).padding(this.outerTopPadding, this.outerRightPadding, this.outerBottomPadding, this.outerLeftPadding).buildStroke();
/*     */ 
/*     */ 
/*     */       
/* 449 */       BorderStroke innerPadding = (new Borders.EmptyBorders(this.parent)).padding(this.innerTopPadding, this.innerRightPadding, this.innerBottomPadding, this.innerLeftPadding).buildStroke();
/*     */       
/* 451 */       this.parent.addBorder(new Borders.StrokeBorder(null, new BorderStroke[] { outerPadding }));
/* 452 */       this.parent.addBorder(new Borders.StrokeBorder(this.title, new BorderStroke[] { innerStroke, outerStroke }));
/* 453 */       this.parent.addBorder(new Borders.StrokeBorder(null, new BorderStroke[] { innerPadding }));
/*     */       
/* 455 */       return this.parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node buildAll() {
/* 465 */       build();
/* 466 */       return this.parent.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class LineBorders
/*     */   {
/*     */     private final Borders parent;
/*     */ 
/*     */     
/*     */     private String title;
/*     */ 
/*     */     
/* 480 */     private BorderStrokeStyle strokeStyle = BorderStrokeStyle.SOLID;
/*     */     
/* 482 */     private Color topColor = Borders.DEFAULT_BORDER_COLOR;
/* 483 */     private Color rightColor = Borders.DEFAULT_BORDER_COLOR;
/* 484 */     private Color bottomColor = Borders.DEFAULT_BORDER_COLOR;
/* 485 */     private Color leftColor = Borders.DEFAULT_BORDER_COLOR;
/*     */     
/* 487 */     private double outerTopPadding = 10.0D;
/* 488 */     private double outerRightPadding = 10.0D;
/* 489 */     private double outerBottomPadding = 10.0D;
/* 490 */     private double outerLeftPadding = 10.0D;
/*     */     
/* 492 */     private double innerTopPadding = 15.0D;
/* 493 */     private double innerRightPadding = 15.0D;
/* 494 */     private double innerBottomPadding = 15.0D;
/* 495 */     private double innerLeftPadding = 15.0D;
/*     */     
/* 497 */     private double topThickness = 1.0D;
/* 498 */     private double rightThickness = 1.0D;
/* 499 */     private double bottomThickness = 1.0D;
/* 500 */     private double leftThickness = 1.0D;
/*     */     
/* 502 */     private double topLeftRadius = 0.0D;
/* 503 */     private double topRightRadius = 0.0D;
/* 504 */     private double bottomRightRadius = 0.0D;
/* 505 */     private double bottomLeftRadius = 0.0D;
/*     */ 
/*     */     
/*     */     private LineBorders(Borders parent) {
/* 509 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders color(Color color) {
/* 516 */       return color(color, color, color, color);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders color(Color topColor, Color rightColor, Color bottomColor, Color leftColor) {
/* 525 */       this.topColor = topColor;
/* 526 */       this.rightColor = rightColor;
/* 527 */       this.bottomColor = bottomColor;
/* 528 */       this.leftColor = leftColor;
/* 529 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders strokeStyle(BorderStrokeStyle strokeStyle) {
/* 540 */       this.strokeStyle = strokeStyle;
/* 541 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders outerPadding(double padding) {
/* 548 */       return outerPadding(padding, padding, padding, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders outerPadding(double topPadding, double rightPadding, double bottomPadding, double leftPadding) {
/* 557 */       this.outerTopPadding = topPadding;
/* 558 */       this.outerRightPadding = rightPadding;
/* 559 */       this.outerBottomPadding = bottomPadding;
/* 560 */       this.outerLeftPadding = leftPadding;
/*     */       
/* 562 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders innerPadding(double padding) {
/* 569 */       return innerPadding(padding, padding, padding, padding);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders innerPadding(double topPadding, double rightPadding, double bottomPadding, double leftPadding) {
/* 578 */       this.innerTopPadding = topPadding;
/* 579 */       this.innerRightPadding = rightPadding;
/* 580 */       this.innerBottomPadding = bottomPadding;
/* 581 */       this.innerLeftPadding = leftPadding;
/*     */       
/* 583 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders thickness(double thickness) {
/* 591 */       return thickness(thickness, thickness, thickness, thickness);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders thickness(double topThickness, double rightThickness, double bottomThickness, double leftThickness) {
/* 600 */       this.topThickness = topThickness;
/* 601 */       this.rightThickness = rightThickness;
/* 602 */       this.bottomThickness = bottomThickness;
/* 603 */       this.leftThickness = leftThickness;
/* 604 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders radius(double radius) {
/* 611 */       return radius(radius, radius, radius, radius);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders radius(double topLeft, double topRight, double bottomRight, double bottomLeft) {
/* 620 */       this.topLeftRadius = topLeft;
/* 621 */       this.topRightRadius = topRight;
/* 622 */       this.bottomRightRadius = bottomRight;
/* 623 */       this.bottomLeftRadius = bottomLeft;
/* 624 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LineBorders title(String title) {
/* 631 */       this.title = title;
/* 632 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Borders build() {
/* 641 */       BorderStroke borderStroke = new BorderStroke((Paint)this.topColor, (Paint)this.rightColor, (Paint)this.bottomColor, (Paint)this.leftColor, this.strokeStyle, this.strokeStyle, this.strokeStyle, this.strokeStyle, new CornerRadii(this.topLeftRadius, this.topRightRadius, this.bottomRightRadius, this.bottomLeftRadius, false), new BorderWidths(this.topThickness, this.rightThickness, this.bottomThickness, this.leftThickness), null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 650 */       BorderStroke outerPadding = (new Borders.EmptyBorders(this.parent)).padding(this.outerTopPadding, this.outerRightPadding, this.outerBottomPadding, this.outerLeftPadding).buildStroke();
/*     */ 
/*     */ 
/*     */       
/* 654 */       BorderStroke innerPadding = (new Borders.EmptyBorders(this.parent)).padding(this.innerTopPadding, this.innerRightPadding, this.innerBottomPadding, this.innerLeftPadding).buildStroke();
/*     */       
/* 656 */       this.parent.addBorder(new Borders.StrokeBorder(null, new BorderStroke[] { outerPadding }));
/* 657 */       this.parent.addBorder(new Borders.StrokeBorder(this.title, new BorderStroke[] { borderStroke }));
/* 658 */       this.parent.addBorder(new Borders.StrokeBorder(null, new BorderStroke[] { innerPadding }));
/*     */       
/* 660 */       return this.parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node buildAll() {
/* 670 */       build();
/* 671 */       return this.parent.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Border
/*     */   {
/*     */     Node wrap(Node param1Node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StrokeBorder
/*     */     implements Border
/*     */   {
/*     */     private static final int TITLE_PADDING = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final double GAP_PADDING = 5.0D;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String title;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final BorderStroke[] borderStrokes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public StrokeBorder(String title, BorderStroke... borderStrokes) {
/* 721 */       this.title = title;
/* 722 */       this.borderStrokes = borderStrokes;
/*     */     }
/*     */     
/*     */     public Node wrap(final Node n) {
/* 726 */       StackPane pane = new StackPane()
/*     */         {
/*     */           Label titleLabel;
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
/*     */           protected void layoutChildren() {
/* 745 */             super.layoutChildren();
/*     */             
/* 747 */             if (this.titleLabel != null) {
/*     */               
/* 749 */               double labelHeight = this.titleLabel.prefHeight(-1.0D);
/* 750 */               double labelWidth = this.titleLabel.prefWidth(labelHeight) + 3.0D;
/* 751 */               this.titleLabel.resize(labelWidth, labelHeight);
/* 752 */               this.titleLabel.relocate(6.0D, -labelHeight / 2.0D - 1.0D);
/*     */               
/* 754 */               List<BorderStroke> newBorderStrokes = new ArrayList<>(2);
/*     */ 
/*     */               
/* 757 */               for (BorderStroke bs : Borders.StrokeBorder.this.borderStrokes) {
/* 758 */                 List<Double> dashList = new ArrayList<>();
/*     */ 
/*     */                 
/* 761 */                 if (bs.getTopStyle().getDashArray().isEmpty()) {
/* 762 */                   dashList.addAll(Arrays.asList(new Double[] { Double.valueOf(5.0D), Double.valueOf(labelWidth), Double.valueOf(Double.MAX_VALUE) }));
/*     */                 } else {
/*     */                   
/* 765 */                   double origDashWidth = bs.getTopStyle().getDashArray().stream().mapToDouble(d -> d.doubleValue()).sum();
/*     */                   
/* 767 */                   if (origDashWidth > 5.0D) {
/* 768 */                     dashList.add(Double.valueOf(5.0D));
/* 769 */                     dashList.add(Double.valueOf(labelWidth));
/*     */                   } else {
/* 771 */                     int no = (int)(5.0D / origDashWidth);
/*     */                     
/* 773 */                     for (int j = 0; j < no; j++) {
/* 774 */                       dashList.addAll(bs.getTopStyle().getDashArray());
/*     */                     }
/* 776 */                     if ((dashList.size() & 0x1) == 0) {
/* 777 */                       dashList.add(Double.valueOf(0.0D));
/*     */                     }
/* 779 */                     dashList.add(Double.valueOf(labelWidth + 5.0D - no * origDashWidth));
/*     */                   } 
/*     */                   
/* 782 */                   for (int i = 0; i < (getWidth() - labelWidth - origDashWidth) / origDashWidth; i++) {
/* 783 */                     dashList.addAll(bs.getTopStyle().getDashArray());
/*     */                   }
/*     */                 } 
/*     */ 
/*     */ 
/*     */                 
/* 789 */                 BorderStrokeStyle topStrokeStyle = new BorderStrokeStyle(bs.getTopStyle().getType(), bs.getTopStyle().getLineJoin(), bs.getTopStyle().getLineCap(), bs.getTopStyle().getMiterLimit(), bs.getTopStyle().getDashOffset(), dashList);
/*     */ 
/*     */                 
/* 792 */                 newBorderStrokes.add(new BorderStroke(bs
/* 793 */                       .getTopStroke(), bs.getRightStroke(), bs.getBottomStroke(), bs.getLeftStroke(), topStrokeStyle, bs
/* 794 */                       .getRightStyle(), bs.getBottomStyle(), bs.getLeftStyle(), bs
/* 795 */                       .getRadii(), bs.getWidths(), null));
/*     */               } 
/*     */               
/* 798 */               setBorder(new javafx.scene.layout.Border(newBorderStrokes.<BorderStroke>toArray(new BorderStroke[newBorderStrokes.size()])));
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 803 */       pane.setBorder(new javafx.scene.layout.Border(this.borderStrokes));
/* 804 */       return (Node)pane;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\tools\Borders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
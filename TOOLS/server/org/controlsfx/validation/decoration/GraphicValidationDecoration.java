/*     */ package org.controlsfx.validation.decoration;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import org.controlsfx.control.decoration.Decoration;
/*     */ import org.controlsfx.control.decoration.GraphicDecoration;
/*     */ import org.controlsfx.validation.Severity;
/*     */ import org.controlsfx.validation.ValidationMessage;
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
/*     */ public class GraphicValidationDecoration
/*     */   extends AbstractValidationDecoration
/*     */ {
/*  61 */   private static final Image ERROR_IMAGE = new Image(GraphicValidationDecoration.class.getResource("/impl/org/controlsfx/control/validation/decoration-error.png").toExternalForm());
/*  62 */   private static final Image WARNING_IMAGE = new Image(GraphicValidationDecoration.class.getResource("/impl/org/controlsfx/control/validation/decoration-warning.png").toExternalForm());
/*  63 */   private static final Image REQUIRED_IMAGE = new Image(GraphicValidationDecoration.class.getResource("/impl/org/controlsfx/control/validation/required-indicator.png").toExternalForm());
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String SHADOW_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String POPUP_SHADOW_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);";
/*     */ 
/*     */   
/*     */   private static final String TOOLTIP_COMMON_EFFECTS = "-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;";
/*     */ 
/*     */   
/*     */   private static final String ERROR_TOOLTIP_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;-fx-background-color: FBEFEF; -fx-text-fill: cc0033; -fx-border-color:cc0033;";
/*     */ 
/*     */   
/*     */   private static final String WARNING_TOOLTIP_EFFECT = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;-fx-background-color: FFFFCC; -fx-text-fill: CC9900; -fx-border-color: CC9900;";
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node createErrorNode() {
/*  85 */     return (Node)new ImageView(ERROR_IMAGE);
/*     */   }
/*     */   
/*     */   protected Node createWarningNode() {
/*  89 */     return (Node)new ImageView(WARNING_IMAGE);
/*     */   }
/*     */   
/*     */   private Node createDecorationNode(ValidationMessage message) {
/*  93 */     Node graphic = (Severity.ERROR == message.getSeverity()) ? createErrorNode() : createWarningNode();
/*  94 */     graphic.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
/*  95 */     Label label = new Label();
/*  96 */     label.setGraphic(graphic);
/*  97 */     label.setTooltip(createTooltip(message));
/*  98 */     label.setAlignment(Pos.CENTER);
/*  99 */     return (Node)label;
/*     */   }
/*     */   
/*     */   protected Tooltip createTooltip(ValidationMessage message) {
/* 103 */     Tooltip tooltip = new Tooltip(message.getText());
/* 104 */     tooltip.setOpacity(0.9D);
/* 105 */     tooltip.setAutoFix(true);
/* 106 */     tooltip.setStyle((Severity.ERROR == message.getSeverity()) ? "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;-fx-background-color: FBEFEF; -fx-text-fill: cc0033; -fx-border-color:cc0033;" : "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 5, 0, 0, 5);-fx-font-weight: bold; -fx-padding: 5; -fx-border-width:1;-fx-background-color: FFFFCC; -fx-text-fill: CC9900; -fx-border-color: CC9900;");
/* 107 */     return tooltip;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<Decoration> createValidationDecorations(ValidationMessage message) {
/* 115 */     return Arrays.asList(new Decoration[] { (Decoration)new GraphicDecoration(createDecorationNode(message), Pos.BOTTOM_LEFT) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<Decoration> createRequiredDecorations(Control target) {
/* 123 */     return Arrays.asList(new Decoration[] { (Decoration)new GraphicDecoration((Node)new ImageView(REQUIRED_IMAGE), Pos.TOP_LEFT, REQUIRED_IMAGE.getWidth() / 2.0D, REQUIRED_IMAGE.getHeight() / 2.0D) });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\validation\decoration\GraphicValidationDecoration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
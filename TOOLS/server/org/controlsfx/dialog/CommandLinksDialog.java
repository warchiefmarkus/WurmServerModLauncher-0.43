/*     */ package org.controlsfx.dialog;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.binding.DoubleBinding;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.geometry.Insets;
/*     */ import javafx.geometry.Pos;
/*     */ import javafx.geometry.VPos;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ButtonBar;
/*     */ import javafx.scene.control.ButtonType;
/*     */ import javafx.scene.control.Dialog;
/*     */ import javafx.scene.control.DialogPane;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.layout.GridPane;
/*     */ import javafx.scene.layout.Pane;
/*     */ import javafx.scene.layout.Priority;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandLinksDialog
/*     */   extends Dialog<ButtonType>
/*     */ {
/*     */   private static final int gapSize = 10;
/*     */   private final Map<ButtonType, CommandLinksButtonType> typeMap;
/*     */   private Label contentTextLabel;
/*     */   
/*     */   public static class CommandLinksButtonType
/*     */   {
/*     */     private final ButtonType buttonType;
/*     */     private final String longText;
/*     */     private final Node graphic;
/*     */     private boolean isHidden = false;
/*     */     
/*     */     public CommandLinksButtonType(String text, boolean isDefault) {
/*  61 */       this(new ButtonType(text, buildButtonData(isDefault)), (String)null);
/*     */     }
/*     */     
/*     */     public CommandLinksButtonType(String text, String longText, boolean isDefault) {
/*  65 */       this(new ButtonType(text, buildButtonData(isDefault)), longText, (Node)null);
/*     */     }
/*     */     
/*     */     public CommandLinksButtonType(String text, String longText, Node graphic, boolean isDefault) {
/*  69 */       this(new ButtonType(text, buildButtonData(isDefault)), longText, graphic);
/*     */     }
/*     */     
/*     */     private CommandLinksButtonType(ButtonType buttonType) {
/*  73 */       this(buttonType, (String)null);
/*     */     }
/*     */     
/*     */     private CommandLinksButtonType(ButtonType buttonType, String longText) {
/*  77 */       this(buttonType, longText, (Node)null);
/*     */     }
/*     */     
/*     */     private CommandLinksButtonType(ButtonType buttonType, String longText, Node graphic) {
/*  81 */       this.buttonType = buttonType;
/*  82 */       this.longText = longText;
/*  83 */       this.graphic = graphic;
/*     */     }
/*     */ 
/*     */     
/*     */     private static ButtonBar.ButtonData buildButtonData(boolean isDeafault) {
/*  88 */       return isDeafault ? ButtonBar.ButtonData.OK_DONE : ButtonBar.ButtonData.OTHER;
/*     */     }
/*     */     
/*     */     private static CommandLinksButtonType buildHiddenCancelLink() {
/*  92 */       CommandLinksButtonType link = new CommandLinksButtonType(new ButtonType("", ButtonBar.ButtonData.CANCEL_CLOSE));
/*  93 */       link.isHidden = true;
/*  94 */       return link;
/*     */     }
/*     */     
/*     */     public ButtonType getButtonType() {
/*  98 */       return this.buttonType;
/*     */     }
/*     */     
/*     */     public Node getGraphic() {
/* 102 */       return this.graphic;
/*     */     }
/*     */     
/*     */     public String getLongText() {
/* 106 */       return this.longText;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 117 */   private GridPane grid = new GridPane() {
/*     */       protected double computePrefWidth(double height) {
/* 119 */         boolean isDefault = true;
/* 120 */         double pw = 0.0D;
/*     */         
/* 122 */         for (ButtonType buttonType : CommandLinksDialog.this.getDialogPane().getButtonTypes()) {
/* 123 */           Button button = (Button)CommandLinksDialog.this.getDialogPane().lookupButton(buttonType);
/* 124 */           double buttonPrefWidth = button.getGraphic().prefWidth(-1.0D);
/*     */           
/* 126 */           if (isDefault) {
/* 127 */             pw = buttonPrefWidth;
/* 128 */             isDefault = false; continue;
/*     */           } 
/* 130 */           pw = Math.min(pw, buttonPrefWidth);
/*     */         } 
/*     */         
/* 133 */         return pw + 10.0D;
/*     */       }
/*     */       
/*     */       protected double computePrefHeight(double width) {
/* 137 */         double ph = (CommandLinksDialog.this.getDialogPane().getHeader() == null) ? 0.0D : 10.0D;
/*     */         
/* 139 */         for (ButtonType buttonType : CommandLinksDialog.this.getDialogPane().getButtonTypes()) {
/* 140 */           Button button = (Button)CommandLinksDialog.this.getDialogPane().lookupButton(buttonType);
/* 141 */           ph += button.prefHeight(width) + 10.0D;
/*     */         } 
/*     */ 
/*     */         
/* 145 */         return ph * 1.2D;
/*     */       }
/*     */     };
/*     */   
/*     */   public CommandLinksDialog(CommandLinksButtonType... links) {
/* 150 */     this(Arrays.asList(links));
/*     */   }
/*     */   
/*     */   public CommandLinksDialog(List<CommandLinksButtonType> links) {
/* 154 */     this.grid.setHgap(10.0D);
/* 155 */     this.grid.setVgap(10.0D);
/* 156 */     this.grid.getStyleClass().add("container");
/*     */     
/* 158 */     DialogPane dialogPane = new DialogPane() {
/*     */         protected Node createButtonBar() {
/* 160 */           return null;
/*     */         }
/*     */         
/*     */         protected Node createButton(ButtonType buttonType) {
/* 164 */           return (Node)CommandLinksDialog.this.createCommandLinksButton(buttonType);
/*     */         }
/*     */       };
/* 167 */     setDialogPane(dialogPane);
/*     */     
/* 169 */     setTitle(Localization.getString("Dialog.info.title"));
/* 170 */     dialogPane.getStyleClass().add("command-links-dialog");
/* 171 */     dialogPane.getStylesheets().add(getClass().getResource("dialogs.css").toExternalForm());
/* 172 */     dialogPane.getStylesheets().add(getClass().getResource("commandlink.css").toExternalForm());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     this.typeMap = new HashMap<>();
/* 178 */     for (CommandLinksButtonType link : links) {
/* 179 */       addLinkToDialog(dialogPane, link);
/*     */     }
/* 181 */     addLinkToDialog(dialogPane, CommandLinksButtonType.buildHiddenCancelLink());
/*     */     
/* 183 */     updateGrid();
/* 184 */     dialogPane.getButtonTypes().addListener(c -> updateGrid());
/*     */     
/* 186 */     contentTextProperty().addListener(o -> updateContentText());
/*     */   }
/*     */   
/*     */   private void addLinkToDialog(DialogPane dialogPane, CommandLinksButtonType link) {
/* 190 */     this.typeMap.put(link.getButtonType(), link);
/* 191 */     dialogPane.getButtonTypes().add(link.getButtonType());
/*     */   }
/*     */   
/*     */   private void updateContentText() {
/* 195 */     String contentText = getDialogPane().getContentText();
/* 196 */     this.grid.getChildren().remove(this.contentTextLabel);
/* 197 */     if (contentText != null && !contentText.isEmpty()) {
/* 198 */       if (this.contentTextLabel != null) {
/* 199 */         this.contentTextLabel.setText(contentText);
/*     */       } else {
/* 201 */         this.contentTextLabel = new Label(getDialogPane().getContentText());
/* 202 */         this.contentTextLabel.getStyleClass().add("command-link-message");
/*     */       } 
/* 204 */       this.grid.add((Node)this.contentTextLabel, 0, 0);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateGrid() {
/* 209 */     this.grid.getChildren().clear();
/*     */ 
/*     */     
/* 212 */     updateContentText();
/*     */ 
/*     */     
/* 215 */     int row = 1;
/* 216 */     for (ButtonType buttonType : getDialogPane().getButtonTypes()) {
/* 217 */       if (buttonType == null)
/*     */         continue; 
/* 219 */       Button button = (Button)getDialogPane().lookupButton(buttonType);
/*     */       
/* 221 */       GridPane.setHgrow((Node)button, Priority.ALWAYS);
/* 222 */       GridPane.setVgrow((Node)button, Priority.ALWAYS);
/* 223 */       this.grid.add((Node)button, 0, row++);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     getDialogPane().setContent((Node)this.grid);
/* 230 */     getDialogPane().requestLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   private Button createCommandLinksButton(ButtonType buttonType) {
/* 235 */     CommandLinksButtonType commandLink = this.typeMap.getOrDefault(buttonType, new CommandLinksButtonType(buttonType));
/*     */ 
/*     */ 
/*     */     
/* 239 */     Button button = new Button();
/* 240 */     button.getStyleClass().addAll((Object[])new String[] { "command-link-button" });
/* 241 */     button.setMaxHeight(Double.MAX_VALUE);
/* 242 */     button.setMaxWidth(Double.MAX_VALUE);
/* 243 */     button.setAlignment(Pos.CENTER_LEFT);
/*     */     
/* 245 */     ButtonBar.ButtonData buttonData = buttonType.getButtonData();
/* 246 */     button.setDefaultButton((buttonData != null && buttonData.isDefaultButton()));
/* 247 */     button.setOnAction(ae -> setResult(buttonType));
/*     */     
/* 249 */     final Label titleLabel = new Label(commandLink.getButtonType().getText());
/* 250 */     titleLabel.minWidthProperty().bind((ObservableValue)new DoubleBinding()
/*     */         {
/*     */ 
/*     */           
/*     */           protected double computeValue()
/*     */           {
/* 256 */             return titleLabel.getPrefWidth() + 400.0D;
/*     */           }
/*     */         });
/* 259 */     titleLabel.getStyleClass().addAll((Object[])new String[] { "line-1" });
/* 260 */     titleLabel.setWrapText(true);
/* 261 */     titleLabel.setAlignment(Pos.TOP_LEFT);
/* 262 */     GridPane.setVgrow((Node)titleLabel, Priority.NEVER);
/*     */     
/* 264 */     Label messageLabel = new Label(commandLink.getLongText());
/* 265 */     messageLabel.getStyleClass().addAll((Object[])new String[] { "line-2" });
/* 266 */     messageLabel.setWrapText(true);
/* 267 */     messageLabel.setAlignment(Pos.TOP_LEFT);
/* 268 */     messageLabel.setMaxHeight(Double.MAX_VALUE);
/* 269 */     GridPane.setVgrow((Node)messageLabel, Priority.SOMETIMES);
/*     */     
/* 271 */     Node commandLinkImage = commandLink.getGraphic();
/*     */     
/* 273 */     Node view = (commandLinkImage == null) ? (Node)new ImageView(CommandLinksDialog.class.getResource("arrow-green-right.png").toExternalForm()) : commandLinkImage;
/*     */     
/* 275 */     Pane graphicContainer = new Pane(new Node[] { view });
/* 276 */     graphicContainer.getStyleClass().add("graphic-container");
/* 277 */     GridPane.setValignment((Node)graphicContainer, VPos.TOP);
/* 278 */     GridPane.setMargin((Node)graphicContainer, new Insets(0.0D, 10.0D, 0.0D, 0.0D));
/*     */     
/* 280 */     GridPane grid = new GridPane();
/* 281 */     grid.minWidthProperty().bind((ObservableValue)titleLabel.prefWidthProperty());
/* 282 */     grid.setMaxHeight(Double.MAX_VALUE);
/* 283 */     grid.setMaxWidth(Double.MAX_VALUE);
/* 284 */     grid.getStyleClass().add("container");
/* 285 */     grid.add((Node)graphicContainer, 0, 0, 1, 2);
/* 286 */     grid.add((Node)titleLabel, 1, 0);
/* 287 */     grid.add((Node)messageLabel, 1, 1);
/*     */     
/* 289 */     button.setGraphic((Node)grid);
/* 290 */     button.minWidthProperty().bind((ObservableValue)titleLabel.prefWidthProperty());
/*     */     
/* 292 */     if (commandLink.isHidden) {
/* 293 */       button.setVisible(false);
/* 294 */       button.setPrefHeight(1.0D);
/*     */     } 
/* 296 */     return button;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\CommandLinksDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
/*     */ package org.controlsfx.dialog;
/*     */ 
/*     */ import impl.org.controlsfx.i18n.Localization;
/*     */ import javafx.application.Platform;
/*     */ import javafx.event.ActionEvent;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Button;
/*     */ import javafx.scene.control.ButtonBar;
/*     */ import javafx.scene.control.ButtonType;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.Dialog;
/*     */ import javafx.scene.control.DialogPane;
/*     */ import javafx.scene.control.Label;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.layout.VBox;
/*     */ import javafx.util.Callback;
/*     */ import javafx.util.Pair;
/*     */ import org.controlsfx.control.textfield.CustomPasswordField;
/*     */ import org.controlsfx.control.textfield.CustomTextField;
/*     */ import org.controlsfx.control.textfield.TextFields;
/*     */ import org.controlsfx.validation.ValidationSupport;
/*     */ import org.controlsfx.validation.Validator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoginDialog
/*     */   extends Dialog<Pair<String, String>>
/*     */ {
/*     */   private final ButtonType loginButtonType;
/*     */   private final CustomTextField txUserName;
/*     */   private final CustomPasswordField txPassword;
/*     */   
/*     */   public LoginDialog(Pair<String, String> initialUserInfo, Callback<Pair<String, String>, Void> authenticator) {
/*  56 */     DialogPane dialogPane = getDialogPane();
/*     */     
/*  58 */     setTitle(Localization.getString("login.dlg.title"));
/*  59 */     dialogPane.setHeaderText(Localization.getString("login.dlg.header"));
/*  60 */     dialogPane.getStyleClass().add("login-dialog");
/*  61 */     dialogPane.getStylesheets().add(LoginDialog.class.getResource("dialogs.css").toExternalForm());
/*  62 */     dialogPane.getButtonTypes().addAll((Object[])new ButtonType[] { ButtonType.CANCEL });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.txUserName = (CustomTextField)TextFields.createClearableTextField();
/*     */     
/*  70 */     this.txUserName.setLeft((Node)new ImageView(LoginDialog.class.getResource("/org/controlsfx/dialog/user.png").toExternalForm()));
/*     */     
/*  72 */     this.txPassword = (CustomPasswordField)TextFields.createClearablePasswordField();
/*  73 */     this.txPassword.setLeft((Node)new ImageView(LoginDialog.class.getResource("/org/controlsfx/dialog/lock.png").toExternalForm()));
/*     */     
/*  75 */     Label lbMessage = new Label("");
/*  76 */     lbMessage.getStyleClass().addAll((Object[])new String[] { "message-banner" });
/*  77 */     lbMessage.setVisible(false);
/*  78 */     lbMessage.setManaged(false);
/*     */     
/*  80 */     VBox content = new VBox(10.0D);
/*  81 */     content.getChildren().add(lbMessage);
/*  82 */     content.getChildren().add(this.txUserName);
/*  83 */     content.getChildren().add(this.txPassword);
/*     */     
/*  85 */     dialogPane.setContent((Node)content);
/*     */     
/*  87 */     this.loginButtonType = new ButtonType(Localization.getString("login.dlg.login.button"), ButtonBar.ButtonData.OK_DONE);
/*  88 */     dialogPane.getButtonTypes().addAll((Object[])new ButtonType[] { this.loginButtonType });
/*  89 */     Button loginButton = (Button)dialogPane.lookupButton(this.loginButtonType);
/*  90 */     loginButton.setOnAction(actionEvent -> {
/*     */           try {
/*     */             if (authenticator != null) {
/*     */               authenticator.call(new Pair(this.txUserName.getText(), this.txPassword.getText()));
/*     */             }
/*     */             
/*     */             lbMessage.setVisible(false);
/*     */             lbMessage.setManaged(false);
/*     */             hide();
/*  99 */           } catch (Throwable ex) {
/*     */             lbMessage.setVisible(true);
/*     */ 
/*     */ 
/*     */             
/*     */             lbMessage.setManaged(true);
/*     */ 
/*     */ 
/*     */             
/*     */             lbMessage.setText(ex.getMessage());
/*     */ 
/*     */ 
/*     */             
/*     */             ex.printStackTrace();
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 118 */     String userNameCation = Localization.getString("login.dlg.user.caption");
/* 119 */     String passwordCaption = Localization.getString("login.dlg.pswd.caption");
/* 120 */     this.txUserName.setPromptText(userNameCation);
/* 121 */     this.txUserName.setText((initialUserInfo == null) ? "" : (String)initialUserInfo.getKey());
/* 122 */     this.txPassword.setPromptText(passwordCaption);
/* 123 */     this.txPassword.setText(new String((initialUserInfo == null) ? "" : (String)initialUserInfo.getValue()));
/*     */     
/* 125 */     ValidationSupport validationSupport = new ValidationSupport();
/* 126 */     Platform.runLater(() -> {
/*     */           String requiredFormat = "'%s' is required";
/*     */           
/*     */           validationSupport.registerValidator((Control)this.txUserName, Validator.createEmptyValidator(String.format(requiredFormat, new Object[] { userNameCation })));
/*     */           
/*     */           validationSupport.registerValidator((Control)this.txPassword, Validator.createEmptyValidator(String.format(requiredFormat, new Object[] { passwordCaption })));
/*     */           
/*     */           this.txUserName.requestFocus();
/*     */         });
/* 135 */     setResultConverter(dialogButton -> (dialogButton == this.loginButtonType) ? new Pair(this.txUserName.getText(), this.txPassword.getText()) : null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\dialog\LoginDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
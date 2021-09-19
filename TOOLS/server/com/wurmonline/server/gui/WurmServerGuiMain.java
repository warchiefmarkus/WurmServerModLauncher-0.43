/*     */ package com.wurmonline.server.gui;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.utils.SimpleArgumentParser;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javafx.application.Application;
/*     */ import javafx.event.Event;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.fxml.FXMLLoader;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.control.TabPane;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.input.TouchEvent;
/*     */ import javafx.scene.input.TouchPoint;
/*     */ import javafx.stage.Stage;
/*     */ import javafx.stage.WindowEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WurmServerGuiMain
/*     */   extends Application
/*     */ {
/*  50 */   private static final Logger logger = Logger.getLogger(WurmServerGuiMain.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/*  66 */     logger.info("WurmServerGuiMain starting");
/*  67 */     HashSet<String> allowedArgStrings = new HashSet<>();
/*  68 */     for (GuiCommandLineArgument argument : GuiCommandLineArgument.values())
/*     */     {
/*  70 */       allowedArgStrings.add(argument.getArgumentString());
/*     */     }
/*     */     
/*  73 */     SimpleArgumentParser parser = new SimpleArgumentParser(args, allowedArgStrings);
/*  74 */     String dbToStart = "";
/*  75 */     if (parser.hasOption(GuiCommandLineArgument.START.getArgumentString())) {
/*  76 */       dbToStart = parser.getOptionValue(GuiCommandLineArgument.START.getArgumentString());
/*  77 */       if (dbToStart == null || dbToStart.isEmpty()) {
/*  78 */         System.err.println("Start param needs to be followed by server dir: Start=<ServerDir>");
/*  79 */         System.exit(1);
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     Servers.arguments = parser;
/*  84 */     String adminPass = "";
/*  85 */     if (parser.hasOption(GuiCommandLineArgument.ADMIN_PWD.getArgumentString())) {
/*     */       
/*  87 */       adminPass = parser.getOptionValue(GuiCommandLineArgument.ADMIN_PWD.getArgumentString());
/*  88 */       if (adminPass == null || adminPass.isEmpty()) {
/*     */         
/*  90 */         System.err.println("The admin password needs to be set or it will not be possible to change the settings within the game.");
/*     */       }
/*     */       else {
/*     */         
/*  94 */         WurmServerGuiController.adminPassword = adminPass;
/*     */       } 
/*     */     } 
/*     */     
/*  98 */     if (!dbToStart.isEmpty()) {
/*     */       
/* 100 */       System.out.println("Should start without GUI here!");
/* 101 */       WurmServerGuiController.startDB(dbToStart);
/*     */     }
/*     */     else {
/*     */       
/* 105 */       Application.launch(WurmServerGuiMain.class, (String[])null);
/*     */     } 
/*     */     
/* 108 */     logger.info("WurmServerGuiMain finished");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Stage primaryStage) {
/*     */     try {
/* 116 */       FXMLLoader loader = new FXMLLoader(WurmServerGuiMain.class.getResource("WurmServerGui.fxml"));
/* 117 */       TabPane page = (TabPane)loader.load();
/* 118 */       Scene scene = new Scene((Parent)page);
/* 119 */       primaryStage.setScene(scene);
/* 120 */       primaryStage.setTitle("Wurm Unlimited Server");
/* 121 */       primaryStage.addEventFilter(TouchEvent.ANY, event -> {
/*     */             event.consume();
/*     */ 
/*     */             
/*     */             TouchPoint touchPoint = event.getTouchPoint();
/*     */             
/*     */             int clickCount = 1;
/*     */             
/*     */             MouseEvent mouseEvent = new MouseEvent(event.getSource(), event.getTarget(), MouseEvent.MOUSE_CLICKED, touchPoint.getX(), touchPoint.getY(), touchPoint.getScreenX(), touchPoint.getScreenY(), MouseButton.PRIMARY, clickCount, false, false, false, false, true, false, false, true, false, false, null);
/*     */             
/*     */             Scene yourScene = primaryStage.getScene();
/*     */             
/*     */             Event.fireEvent((EventTarget)yourScene.getRoot(), (Event)mouseEvent);
/*     */           });
/*     */       
/* 136 */       List<Image> iconsList = new ArrayList<>();
/* 137 */       iconsList.add(new Image("com/wurmonline/server/gui/img/icon2_16.png"));
/* 138 */       iconsList.add(new Image("com/wurmonline/server/gui/img/icon2_32.png"));
/* 139 */       iconsList.add(new Image("com/wurmonline/server/gui/img/icon2_64.png"));
/* 140 */       iconsList.add(new Image("com/wurmonline/server/gui/img/icon2_128.png"));
/* 141 */       primaryStage.getIcons().addAll(iconsList);
/*     */       
/* 143 */       primaryStage.show();
/* 144 */       WurmServerGuiController controller = (WurmServerGuiController)loader.getController();
/* 145 */       controller.setStage(primaryStage);
/* 146 */       scene.getWindow().setOnCloseRequest(ev -> {
/*     */             
/*     */             if (!controller.shutdown()) {
/*     */               ev.consume();
/*     */             }
/*     */           });
/* 152 */     } catch (IOException ex) {
/*     */       
/* 154 */       logger.log(Level.SEVERE, ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\WurmServerGuiMain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
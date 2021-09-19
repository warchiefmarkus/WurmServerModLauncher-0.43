/*      */ package com.wurmonline.server.gui;
/*      */ 
/*      */ import coffee.keenan.network.helpers.address.AddressHelper;
/*      */ import coffee.keenan.network.helpers.port.Port;
/*      */ import coffee.keenan.network.helpers.port.PortHelper;
/*      */ import coffee.keenan.network.helpers.port.Protocol;
/*      */ import coffee.keenan.network.validators.address.IAddressValidator;
/*      */ import coffee.keenan.network.validators.address.IP4Validator;
/*      */ import coffee.keenan.network.wrappers.upnp.UPNPService;
/*      */ import com.wurmonline.server.Constants;
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.ServerDirInfo;
/*      */ import com.wurmonline.server.ServerEntry;
/*      */ import com.wurmonline.server.ServerLauncher;
/*      */ import com.wurmonline.server.ServerProperties;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.console.CommandReader;
/*      */ import com.wurmonline.server.gui.folders.DistEntity;
/*      */ import com.wurmonline.server.gui.folders.Folders;
/*      */ import com.wurmonline.server.gui.folders.GameFolder;
/*      */ import com.wurmonline.server.gui.folders.PresetFolder;
/*      */ import com.wurmonline.server.gui.propertysheet.PlayerPropertySheet;
/*      */ import com.wurmonline.server.gui.propertysheet.ServerPropertySheet;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.PlayerInfo;
/*      */ import com.wurmonline.server.players.PlayerInfoFactory;
/*      */ import com.wurmonline.server.steam.SteamHandler;
/*      */ import com.wurmonline.shared.constants.SteamVersion;
/*      */ import java.io.IOException;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.RuntimeMXBean;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.InetAddress;
/*      */ import java.net.UnknownHostException;
/*      */ import java.nio.file.Paths;
/*      */ import java.rmi.registry.LocateRegistry;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.EnumMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javafx.beans.value.ObservableValue;
/*      */ import javafx.event.ActionEvent;
/*      */ import javafx.event.Event;
/*      */ import javafx.fxml.FXML;
/*      */ import javafx.scene.Node;
/*      */ import javafx.scene.control.Alert;
/*      */ import javafx.scene.control.Button;
/*      */ import javafx.scene.control.ButtonType;
/*      */ import javafx.scene.control.CheckBox;
/*      */ import javafx.scene.control.ComboBox;
/*      */ import javafx.scene.control.Label;
/*      */ import javafx.scene.control.ScrollPane;
/*      */ import javafx.scene.control.Tab;
/*      */ import javafx.scene.control.TabPane;
/*      */ import javafx.scene.control.TextArea;
/*      */ import javafx.scene.control.TextField;
/*      */ import javafx.scene.layout.AnchorPane;
/*      */ import javafx.scene.layout.GridPane;
/*      */ import javafx.scene.layout.HBox;
/*      */ import javafx.scene.layout.Priority;
/*      */ import javafx.scene.text.Text;
/*      */ import javafx.stage.Stage;
/*      */ import javafx.stage.StageStyle;
/*      */ import sun.management.VMManagement;
/*      */ 
/*      */ public final class WurmServerGuiController
/*      */ {
/*   80 */   private static final Logger logger = Logger.getLogger(WurmServerGuiController.class.getName());
/*      */   private static ServerPropertySheet lServerPropertySheet;
/*      */   private static ServerPropertySheet localServerPropertySheet;
/*      */   private static PlayerPropertySheet playerPropertySheet;
/*   84 */   private ConcurrentHashMap<Integer, ServerEntry> servers = new ConcurrentHashMap<>();
/*   85 */   private ConcurrentHashMap<Integer, ServerEntry> selectionBoxServers = new ConcurrentHashMap<>();
/*   86 */   public static String adminPassword = "";
/*      */   
/*      */   private ServerEntry nullServer;
/*      */   
/*      */   private ServerLauncher launcher;
/*      */   
/*      */   private Stage primaryStage;
/*      */   
/*      */   @FXML
/*      */   Button updateGameBtn;
/*      */   
/*      */   @FXML
/*      */   Label updateRequiredLabel;
/*      */   
/*      */   @FXML
/*      */   GridPane gameControls;
/*      */   
/*      */   @FXML
/*      */   GridPane runningControls;
/*      */   
/*      */   @FXML
/*      */   Tab localServerTab;
/*      */   
/*      */   @FXML
/*      */   Tab serverNeighborTab;
/*      */   
/*      */   @FXML
/*      */   Tab serverTravelTab;
/*      */   
/*      */   @FXML
/*      */   ScrollPane tab3ScrollPane1;
/*      */   
/*      */   @FXML
/*      */   AnchorPane tab3ContentPane1;
/*      */   
/*      */   @FXML
/*      */   Tab playersTab;
/*      */   
/*      */   @FXML
/*      */   TabPane rootTabPane;
/*      */   
/*      */   @FXML
/*      */   private AnchorPane tab1ContentPane;
/*      */   
/*      */   @FXML
/*      */   private AnchorPane tab2ContentPane;
/*      */   
/*      */   @FXML
/*      */   private AnchorPane tab3ContentPane;
/*      */   
/*      */   @FXML
/*      */   private ScrollPane tab1ScrollPane;
/*      */   
/*      */   @FXML
/*      */   private ScrollPane tab2ScrollPane;
/*      */   
/*      */   @FXML
/*      */   private ScrollPane tab3ScrollPane;
/*      */   
/*      */   @FXML
/*      */   private AnchorPane tab5ContentPane;
/*      */   @FXML
/*      */   private ScrollPane tab5ScrollPane;
/*      */   @FXML
/*      */   private Button saveServerButton;
/*      */   
/*      */   @FXML
/*      */   void startBtnClicked(ActionEvent event) {
/*  154 */     startGame(false); } @FXML private Button saveServerButtonLocal; @FXML private Text localServerText; @FXML private Button startBtn; @FXML private Button startOfflineBtn; @FXML private Button deleteServerBtn; @FXML private Button deleteBtn; @FXML private CheckBox chkAutoNetwork; private static final Map<GuiCommandLineArgument, ArgumentApplication> ARGUMENT_APPLICATIONS; @FXML private Button shutdownButton; @FXML private Button btnCopy; @FXML private Button savePlayerBtn; @FXML private Button btnRename; @FXML private ComboBox<String> selectServerBox; @FXML private ComboBox<String> selectNeighbourBoxNorth; @FXML private ComboBox<String> selectNeighbourBoxSouth; @FXML
/*      */   private ComboBox<String> selectNeighbourBoxWest; @FXML
/*      */   private ComboBox<String> selectNeighbourBoxEast; @FXML
/*      */   private ComboBox<String> databaseComboBox; @FXML
/*      */   private TextField renameTxtfield; @FXML
/*      */   private TextField secondsTxtField; @FXML
/*      */   private TextField reasonTxtfield; @FXML
/*      */   private TextField copyTextField; @FXML
/*      */   private ComboBox<String> selectPlayerComboBox; @FXML
/*      */   private Button saveNeighboursButton; @FXML
/*  164 */   public void startOfflineBtnClicked() { startGame(true); }
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   public void updateGameBtnClicked() {
/*  170 */     this.updateGameBtn.setText("Updating...");
/*  171 */     this.updateGameBtn.setDisable(true);
/*  172 */     DbConnector.performMigrations();
/*  173 */     this.updateGameBtn.setText("Updated");
/*  174 */     this.updateRequiredLabel.setVisible(false);
/*  175 */     this.startBtn.setDisable(false);
/*  176 */     this.startOfflineBtn.setDisable(false);
/*  177 */     this.chkAutoNetwork.setVisible(true);
/*  178 */     setDisableTabs(false);
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
/*      */   static {
/*  190 */     EnumMap<GuiCommandLineArgument, ArgumentApplication> applications = new EnumMap<>(GuiCommandLineArgument.class);
/*      */     
/*  192 */     applications.put(GuiCommandLineArgument.IP_ADDR, ip -> {
/*      */           Servers.localServer.EXTERNALIP = ip;
/*      */           
/*      */           Servers.localServer.INTRASERVERADDRESS = ip;
/*      */           
/*      */           logger.log(Level.INFO, "Internal and External IP set to: " + ip);
/*      */         });
/*  199 */     applications.put(GuiCommandLineArgument.EXTERNAL_PORT, externalPort -> {
/*      */           Servers.localServer.EXTERNALPORT = externalPort;
/*      */           
/*      */           logger.log(Level.INFO, "External port set to: " + externalPort);
/*      */         });
/*      */     
/*  205 */     applications.put(GuiCommandLineArgument.INTERNAL_PORT, internalPort -> {
/*      */           Servers.localServer.INTRASERVERPORT = internalPort;
/*      */           
/*      */           logger.log(Level.INFO, "Internal port set to: " + internalPort);
/*      */         });
/*      */     
/*  211 */     applications.put(GuiCommandLineArgument.EPIC_SETTINGS, epicSettingStr -> {
/*      */           boolean epic = Boolean.parseBoolean(epicSettingStr);
/*      */           
/*      */           Servers.localServer.EPIC = epic;
/*      */           
/*      */           logger.log(Level.INFO, "Epic settings set to: " + epic);
/*      */         });
/*  218 */     applications.put(GuiCommandLineArgument.HOME_KINGDOM, kIdStr -> {
/*      */           byte kId = Byte.parseByte(kIdStr);
/*      */           
/*      */           Servers.localServer.KINGDOM = kId;
/*      */           
/*      */           String kName = Kingdoms.getNameFor(kId);
/*      */           logger.log(Level.INFO, "Home server kingdom id set to: " + kId + " name: " + kName);
/*      */         });
/*  226 */     applications.put(GuiCommandLineArgument.HOME_SERVER, isHomeStr -> {
/*      */           boolean isHome = Boolean.parseBoolean(isHomeStr);
/*      */           
/*      */           Servers.localServer.HOMESERVER = isHome;
/*      */           
/*      */           logger.log(Level.INFO, "Is home server: " + isHome);
/*      */         });
/*  233 */     applications.put(GuiCommandLineArgument.LOGIN_SERVER, isLoginStr -> {
/*      */           boolean isLogin = Boolean.parseBoolean(isLoginStr);
/*      */           
/*      */           Servers.localServer.LOGINSERVER = isLogin;
/*      */           
/*      */           logger.log(Level.INFO, "Is loginserver: " + isLogin);
/*      */         });
/*  240 */     applications.put(GuiCommandLineArgument.PLAYER_NUM, playerNumStr -> {
/*      */           int playerLimit = Integer.parseInt(playerNumStr);
/*      */           
/*      */           Servers.localServer.pLimit = playerLimit;
/*      */           
/*      */           Servers.localServer.playerLimitOverridable = false;
/*      */           logger.log(Level.INFO, "Player Limit: " + playerLimit);
/*      */         });
/*  248 */     applications.put(GuiCommandLineArgument.PVP, pvpStr -> {
/*      */           boolean isPvP = Boolean.parseBoolean(pvpStr);
/*      */           
/*      */           Servers.localServer.PVPSERVER = isPvP;
/*      */           
/*      */           logger.log(Level.INFO, "Allow PvP: " + isPvP);
/*      */         });
/*  255 */     applications.put(GuiCommandLineArgument.QUERY_PORT, qPortStr -> {
/*      */           ServerProperties.loadProperties();
/*      */           
/*      */           ServerProperties.setValue("STEAMQUERYPORT", qPortStr);
/*      */           
/*      */           Servers.localServer.STEAMQUERYPORT = qPortStr;
/*      */           logger.log(Level.INFO, "Steam query port: " + qPortStr);
/*      */         });
/*  263 */     applications.put(GuiCommandLineArgument.RMI_PORT, rmiPortStr -> {
/*      */           int rmiPort = Integer.parseInt(rmiPortStr);
/*      */           
/*      */           Servers.localServer.RMI_PORT = rmiPort;
/*      */           
/*      */           logger.log(Level.INFO, "RMI port: " + rmiPort);
/*      */         });
/*  270 */     applications.put(GuiCommandLineArgument.RMI_REG, rmiPortStr -> {
/*      */           int rmiPort = Integer.parseInt(rmiPortStr);
/*      */           
/*      */           Servers.localServer.REGISTRATION_PORT = rmiPort;
/*      */           
/*      */           logger.log(Level.INFO, "RMI register port: " + rmiPort);
/*      */         });
/*  277 */     applications.put(GuiCommandLineArgument.SERVER_NAME, sName -> {
/*      */           Servers.localServer.name = sName;
/*      */           
/*      */           logger.log(Level.INFO, "Server broadcast name: " + sName);
/*      */         });
/*      */     
/*  283 */     applications.put(GuiCommandLineArgument.SERVER_PASS, sPass -> Servers.localServer.setSteamServerPassword(sPass));
/*      */     
/*  285 */     ARGUMENT_APPLICATIONS = Collections.unmodifiableMap(applications);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean applyArguments() {
/*  295 */     boolean hasChanged = false;
/*      */     
/*  297 */     for (GuiCommandLineArgument commandLineArgument : ARGUMENT_APPLICATIONS.keySet()) {
/*      */       
/*  299 */       String argumentString = commandLineArgument.getArgumentString();
/*  300 */       if (Servers.arguments.hasOption(argumentString)) {
/*      */         
/*  302 */         ((ArgumentApplication)ARGUMENT_APPLICATIONS.get(commandLineArgument)).applyArgument(Servers.arguments
/*  303 */             .getOptionValue(argumentString));
/*  304 */         hasChanged = true;
/*      */       } 
/*      */     } 
/*  307 */     return hasChanged;
/*      */   }
/*      */ 
/*      */   
/*      */   static void initServer(String dbName) {
/*  312 */     Servers.loadAllServers(true);
/*      */ 
/*      */     
/*  315 */     for (ServerEntry entry : Servers.getAllServers()) {
/*      */       
/*  317 */       if (entry.name.equals(dbName)) {
/*      */         
/*  319 */         Servers.localServer = entry;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  324 */     if (Servers.localServer != null && Servers.arguments != null) {
/*      */       
/*  326 */       logger.log(Level.INFO, "Setting server settings from command line");
/*      */       
/*  328 */       if (applyArguments())
/*      */       {
/*  330 */         Servers.localServer.saveNewGui(Servers.localServer.id);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void deleteBtnClicked(ActionEvent event) {
/*  343 */     GameFolder orig = Folders.getCurrent();
/*  344 */     if (orig == null)
/*      */       return; 
/*  346 */     Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
/*  347 */     alert.setTitle("Delete Game");
/*  348 */     alert.setHeaderText("This will delete the game " + orig.getName());
/*  349 */     alert.setContentText("Are you really sure you want to do this?");
/*      */     
/*  351 */     Optional<ButtonType> result = alert.showAndWait();
/*  352 */     if (result.isPresent() && result.get() == ButtonType.OK) {
/*      */       
/*  354 */       DbConnector.closeAll();
/*  355 */       if (!orig.delete()) {
/*      */         
/*  357 */         showErrorDialog("Delete", "Unable to delete the game", false);
/*      */         return;
/*      */       } 
/*  360 */       Folders.removeGame(orig);
/*  361 */       ArrayList<GameFolder> games = Folders.getGameFolders();
/*  362 */       if (games.size() > 0)
/*  363 */         setCurrent(games.get(0)); 
/*  364 */       buildDatabaseComboBox(true);
/*      */     } 
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
/*      */   @FXML
/*      */   void shutdownButtonClicked(ActionEvent event) {
/*  383 */     if (this.launcher != null) {
/*      */       
/*  385 */       this.launcher.getServer().startShutdown(Integer.valueOf(this.secondsTxtField.getText()).intValue(), this.reasonTxtfield.getText());
/*  386 */       System.out.println("The server is shutting down in " + Integer.valueOf(this.secondsTxtField.getText()) + " seconds. " + this.reasonTxtfield.getText());
/*      */     }
/*      */     else {
/*      */       
/*  390 */       showErrorDialog("Could not shut down", "The system found no server to shut down.", false);
/*      */     } 
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
/*      */   @FXML
/*      */   void btnCopyClicked(ActionEvent event) {
/*  409 */     if (Folders.getCurrent() == null) {
/*      */       
/*  411 */       showErrorDialog("Copy Game", "Could not copy game, no game selected.", false);
/*      */       return;
/*      */     } 
/*  414 */     GameFolder copyTo = new GameFolder(Folders.getGamesPath().resolve(this.copyTextField.getText()));
/*  415 */     if (!copyTo.create()) {
/*      */       
/*  417 */       showErrorDialog("Copy Game", "Could not create new game folder", false);
/*      */       
/*      */       return;
/*      */     } 
/*  421 */     if (!Folders.getCurrent().copyTo(copyTo.getPath())) {
/*  422 */       showErrorDialog("Copy Game", "Failed to copy game to new folder. Please see logs.", false);
/*      */     } else {
/*  424 */       Folders.addGame(copyTo);
/*  425 */     }  buildDatabaseComboBox(false);
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
/*      */   private void showErrorDialog(String headerText, String contentText, boolean isResizable) {
/*  437 */     Alert alert2 = new Alert(Alert.AlertType.ERROR);
/*  438 */     alert2.initStyle(StageStyle.UTILITY);
/*  439 */     alert2.setTitle("Error");
/*  440 */     alert2.setHeaderText(headerText);
/*  441 */     alert2.setContentText(contentText);
/*  442 */     alert2.setResizable(isResizable);
/*  443 */     alert2.showAndWait();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void playerTabSelected(Event event) {
/*  454 */     buildSelectPlayerBox(true);
/*  455 */     buildPlayerPropertyTab(true);
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
/*      */   @FXML
/*      */   void savePlayerBtnClicked(ActionEvent event) {
/*  473 */     logger.info("savePlayerlButtonClicked " + event);
/*  474 */     String error = playerPropertySheet.save();
/*  475 */     if (error != null && error.length() > 0 && error.equalsIgnoreCase("ok")) {
/*      */       
/*  477 */       Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
/*  478 */       alert2.setTitle("Player saved");
/*  479 */       alert2.setHeaderText("Updated accordingly");
/*  480 */       alert2.setContentText("The player was saved.");
/*  481 */       alert2.showAndWait();
/*  482 */       if (this.launcher != null && this.launcher.wasStarted())
/*      */       {
/*  484 */         if (this.launcher.getServer() != null)
/*      */         {
/*  486 */           if (playerPropertySheet.getCurrentData() != null) {
/*      */             
/*  488 */             PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(playerPropertySheet.getCurrentData().getWurmid());
/*  489 */             if (info != null) {
/*      */               
/*  491 */               info.loaded = false;
/*      */               
/*      */               try {
/*  494 */                 info.load();
/*      */               }
/*  496 */               catch (IOException iox) {
/*      */                 
/*  498 */                 logger.log(Level.WARNING, "Failed to loadGames player info for " + info.getName(), iox);
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         }
/*      */       }
/*      */       return;
/*      */     } 
/*  506 */     if (error != null && error.length() > 0) {
/*      */       
/*  508 */       Alert alert2 = new Alert(Alert.AlertType.ERROR);
/*  509 */       alert2.setTitle("Error Dialog");
/*  510 */       alert2.setHeaderText("Could not save player");
/*  511 */       alert2.setContentText("There was an error trying to save the player. This error was reported: " + error + ".");
/*      */       
/*  513 */       alert2.showAndWait();
/*      */       return;
/*      */     } 
/*  516 */     this.savePlayerBtn.setDisable(true);
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
/*      */   public static final void setCurrentFolder(GameFolder folder) {
/*  529 */     if (Folders.getCurrent() == null || Folders.getCurrent() != folder)
/*  530 */       Folders.setCurrent(folder); 
/*  531 */     ServerDirInfo.setPath(folder.getPath());
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setCurrent(GameFolder folder) {
/*  536 */     setCurrentFolder(folder);
/*  537 */     setStageTitle("(" + folder.getName() + ") Wurm Unlimited Server");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void btnRenameClicked(ActionEvent event) {
/*  548 */     GameFolder orig = Folders.getCurrent();
/*  549 */     if (orig == null || !orig.exists()) {
/*      */       
/*  551 */       showErrorDialog("Rename", "No game selected or game does not exist", false);
/*      */       return;
/*      */     } 
/*  554 */     GameFolder dest = new GameFolder(Folders.getGamesPath().resolve(this.renameTxtfield.getText()));
/*  555 */     if (dest.exists()) {
/*      */       
/*  557 */       showErrorDialog("Rename", "A game with that name already exists", false);
/*      */       return;
/*      */     } 
/*  560 */     if (!dest.create()) {
/*      */       
/*  562 */       showErrorDialog("Rename", "Unable to rename the folder: could not create destination folder", false);
/*      */       
/*      */       return;
/*      */     } 
/*  566 */     DbConnector.closeAll();
/*  567 */     if (orig.copyTo(dest.getPath())) {
/*      */       
/*  569 */       if (!orig.delete()) {
/*  570 */         showErrorDialog("Rename", "Could not delete original", false);
/*      */       }
/*      */     } else {
/*      */       
/*  574 */       showErrorDialog("Rename", "Could create destination", false);
/*  575 */       setCurrent(orig);
/*      */       
/*      */       return;
/*      */     } 
/*  579 */     if (!dest.getError().isEmpty()) {
/*      */       
/*  581 */       showErrorDialog("Rename", "Incomplete move: " + dest.getError(), false);
/*  582 */       dest.delete();
/*      */       return;
/*      */     } 
/*  585 */     Folders.addGame(dest);
/*  586 */     setCurrent(dest);
/*  587 */     Folders.removeGame(orig);
/*  588 */     buildDatabaseComboBox(false);
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void secondsTextFieldChanged(ActionEvent event) {
/*  659 */     logger.info("secondsTextFieldChanged " + event);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void reasonTextFieldChanged(ActionEvent event) {
/*  670 */     logger.info("reasonTextFieldChanged " + event);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void renameTxtfieldChanged(ActionEvent event) {
/*  681 */     logger.info("renameTxtfieldChanged " + event);
/*  682 */     this.btnRename.setDisable(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void copyTextFieldChanged(ActionEvent event) {
/*  693 */     logger.info("copyTextFieldChanged " + event);
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
/*      */   public final int getPid() {
/*      */     try {
/*  708 */       RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
/*  709 */       Field jvm = runtime.getClass().getDeclaredField("jvm");
/*  710 */       jvm.setAccessible(true);
/*      */       
/*  712 */       VMManagement mgmt = (VMManagement)jvm.get(runtime);
/*      */       
/*  714 */       Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId", new Class[0]);
/*  715 */       pid_method.setAccessible(true);
/*      */       
/*  717 */       return ((Integer)pid_method.invoke(mgmt, new Object[0])).intValue();
/*      */     }
/*  719 */     catch (Exception exception) {
/*      */ 
/*      */ 
/*      */       
/*  723 */       return 0;
/*      */     } 
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
/*      */   @FXML
/*      */   void selectPlayerComboBoxChanged(ActionEvent event) {
/*  741 */     this.savePlayerBtn.setDisable(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectNeighbourBoxSouthChanged(ActionEvent event) {
/*  752 */     this.saveNeighboursButton.setDisable(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectNeighbourBoxEastChanged(ActionEvent event) {
/*  763 */     this.saveNeighboursButton.setDisable(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectNeighbourBoxNorthChanged(ActionEvent event) {
/*  774 */     this.saveNeighboursButton.setDisable(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectNeighbourBoxWestChanged(ActionEvent event) {
/*  785 */     this.saveNeighboursButton.setDisable(false);
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
/*      */   @FXML
/*      */   void saveNeighboursButtonClicked(ActionEvent event) {
/*  803 */     int index = this.selectNeighbourBoxWest.getSelectionModel().getSelectedIndex();
/*  804 */     if (index > 0) {
/*      */       
/*  806 */       ServerEntry entry = this.selectionBoxServers.get(Integer.valueOf(index));
/*  807 */       if (Servers.localServer.serverWest != entry) {
/*  808 */         Servers.addServerNeighbour(entry.id, "WEST");
/*      */       }
/*  810 */     } else if (Servers.localServer.serverWest != null) {
/*  811 */       Servers.deleteServerNeighbour("WEST");
/*      */     } 
/*  813 */     index = this.selectNeighbourBoxNorth.getSelectionModel().getSelectedIndex();
/*  814 */     if (index > 0) {
/*      */       
/*  816 */       ServerEntry entry = this.selectionBoxServers.get(Integer.valueOf(index));
/*  817 */       if (Servers.localServer.serverNorth != entry) {
/*  818 */         Servers.addServerNeighbour(entry.id, "NORTH");
/*      */       }
/*  820 */     } else if (Servers.localServer.serverNorth != null) {
/*  821 */       Servers.deleteServerNeighbour("NORTH");
/*      */     } 
/*  823 */     index = this.selectNeighbourBoxSouth.getSelectionModel().getSelectedIndex();
/*  824 */     if (index > 0) {
/*      */       
/*  826 */       ServerEntry entry = this.selectionBoxServers.get(Integer.valueOf(index));
/*  827 */       if (Servers.localServer.serverSouth != entry) {
/*  828 */         Servers.addServerNeighbour(entry.id, "SOUTH");
/*      */       }
/*  830 */     } else if (Servers.localServer.serverSouth != null) {
/*  831 */       Servers.deleteServerNeighbour("SOUTH");
/*      */     } 
/*  833 */     index = this.selectNeighbourBoxEast.getSelectionModel().getSelectedIndex();
/*  834 */     if (index > 0) {
/*      */       
/*  836 */       ServerEntry entry = this.selectionBoxServers.get(Integer.valueOf(index));
/*  837 */       if (Servers.localServer.serverEast != entry) {
/*  838 */         Servers.addServerNeighbour(entry.id, "EAST");
/*      */       }
/*  840 */     } else if (Servers.localServer.serverEast != null) {
/*  841 */       Servers.deleteServerNeighbour("EAST");
/*      */     } 
/*  843 */     this.saveNeighboursButton.setDisable(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void databaseComboBoxChanged(ActionEvent event) {
/*  854 */     if (this.rebuilding)
/*      */       return; 
/*  856 */     GameFolder folder = Folders.getGameFolder((String)this.databaseComboBox.getValue());
/*  857 */     DbConnector.closeAll();
/*  858 */     setCurrent(folder);
/*  859 */     buildDatabaseComboBox(true);
/*  860 */     String gameErr = Folders.getCurrent().getError();
/*  861 */     if (!gameErr.isEmpty()) {
/*  862 */       showErrorDialog("Corrupt game folder", gameErr, true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void menuAboutActionPerformed(ActionEvent event) {
/*  873 */     logger.info("menuAboutActionPerformed " + event);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void menuQuitActionPerformed(ActionEvent event) {
/*  884 */     if (this.launcher != null && this.launcher.wasStarted()) {
/*      */       
/*  886 */       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
/*  887 */       alert.setTitle("Stop Server");
/*  888 */       alert.setHeaderText("This will shut down the server.");
/*  889 */       alert.setContentText("Are you really sure you want to do this?");
/*      */       
/*  891 */       Optional<ButtonType> result = alert.showAndWait();
/*  892 */       if (result.isPresent() && result.get() == ButtonType.OK)
/*      */       {
/*  894 */         System.out.println("The server is shutting down");
/*  895 */         this.launcher.getServer().shutDown();
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  900 */       DbConnector.closeAll();
/*  901 */       System.exit(0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void deleteServerBtnClicked(ActionEvent event) {
/*  913 */     int index = this.selectServerBox.getSelectionModel().getSelectedIndex();
/*  914 */     ServerEntry entry = this.servers.get(Integer.valueOf(index));
/*  915 */     if (entry != null) {
/*      */       
/*  917 */       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
/*  918 */       alert.setTitle("Delete Server");
/*  919 */       alert.setHeaderText("This will delete the server " + entry.getName());
/*  920 */       alert.setContentText("Are you really sure you want to do this?");
/*      */       
/*  922 */       Optional<ButtonType> result = alert.showAndWait();
/*  923 */       if (result.isPresent() && result.get() == ButtonType.OK)
/*      */       {
/*  925 */         Servers.deleteServerEntry(entry.getId());
/*  926 */         Servers.deleteServerNeighbour(entry.getId());
/*  927 */         Servers.loadNeighbours();
/*  928 */         buildSelectServerBox(true);
/*      */         
/*  930 */         lServerPropertySheet = new ServerPropertySheet(this.servers.get(Integer.valueOf(0)));
/*  931 */         System.out.println("Property sheet using " + ((ServerEntry)this.servers.get(Integer.valueOf(0))).getName());
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  937 */       Alert alert2 = new Alert(Alert.AlertType.ERROR);
/*  938 */       alert2.setTitle("Error Dialog");
/*  939 */       alert2.setHeaderText("No such server");
/*  940 */       alert2.setContentText("Please select the server to delete in the drop down.");
/*  941 */       alert2.showAndWait();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void saveServerButtonLocalClicked(ActionEvent event) {
/*  953 */     logger.info("saveServerLocalButtonClicked " + event);
/*  954 */     String error = localServerPropertySheet.save();
/*  955 */     if (error != null && error.length() > 0 && error.equalsIgnoreCase("properties saved")) {
/*      */       
/*  957 */       Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
/*  958 */       alert2.setTitle("Properties saved");
/*  959 */       alert2.setHeaderText("Updated accordingly");
/*  960 */       alert2.setContentText("The settings were saved.");
/*  961 */       alert2.showAndWait();
/*      */     }
/*  963 */     else if (error != null && error.length() > 0) {
/*      */       
/*  965 */       Alert alert2 = new Alert(Alert.AlertType.ERROR);
/*  966 */       alert2.setTitle("Error Dialog");
/*  967 */       alert2.setHeaderText("Could not save");
/*  968 */       alert2.setContentText("Errors were reported when saving.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  975 */       Label label = new Label("These are the errors:");
/*      */       
/*  977 */       TextArea textArea = new TextArea(error);
/*  978 */       textArea.setEditable(false);
/*  979 */       textArea.setWrapText(true);
/*      */       
/*  981 */       textArea.setMaxWidth(Double.MAX_VALUE);
/*  982 */       textArea.setMaxHeight(Double.MAX_VALUE);
/*  983 */       GridPane.setVgrow((Node)textArea, Priority.ALWAYS);
/*  984 */       GridPane.setHgrow((Node)textArea, Priority.ALWAYS);
/*      */       
/*  986 */       GridPane expContent = new GridPane();
/*  987 */       expContent.setMaxWidth(Double.MAX_VALUE);
/*  988 */       expContent.add((Node)label, 0, 0);
/*  989 */       expContent.add((Node)textArea, 0, 1);
/*      */ 
/*      */       
/*  992 */       alert2.getDialogPane().setExpandableContent((Node)expContent);
/*      */       
/*  994 */       alert2.showAndWait();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void saveServerButtonClicked(ActionEvent event) {
/* 1006 */     logger.info("saveServerButtonClicked " + event);
/* 1007 */     String error = lServerPropertySheet.save();
/* 1008 */     if (error != null && error.length() > 0 && !error.toLowerCase(Locale.ENGLISH).contains("invalid")) {
/*      */       
/* 1010 */       Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
/* 1011 */       alert2.setTitle("Properties saved");
/* 1012 */       alert2.setHeaderText("Updated accordingly");
/* 1013 */       alert2.setContentText(error);
/* 1014 */       alert2.showAndWait();
/* 1015 */       buildSelectServerBox(true);
/*      */     }
/* 1017 */     else if (error != null && error.length() > 0) {
/*      */       
/* 1019 */       Alert alert2 = new Alert(Alert.AlertType.ERROR);
/* 1020 */       alert2.setTitle("Error Dialog");
/* 1021 */       alert2.setHeaderText("Could not save");
/* 1022 */       alert2.setContentText("Errors were reported when saving.");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1029 */       Label label = new Label("These are the errors:");
/*      */       
/* 1031 */       TextArea textArea = new TextArea(error);
/* 1032 */       textArea.setEditable(false);
/* 1033 */       textArea.setWrapText(true);
/*      */       
/* 1035 */       textArea.setMaxWidth(Double.MAX_VALUE);
/* 1036 */       textArea.setMaxHeight(Double.MAX_VALUE);
/* 1037 */       GridPane.setVgrow((Node)textArea, Priority.ALWAYS);
/* 1038 */       GridPane.setHgrow((Node)textArea, Priority.ALWAYS);
/*      */       
/* 1040 */       GridPane expContent = new GridPane();
/* 1041 */       expContent.setMaxWidth(Double.MAX_VALUE);
/* 1042 */       expContent.add((Node)label, 0, 0);
/* 1043 */       expContent.add((Node)textArea, 0, 1);
/*      */ 
/*      */       
/* 1046 */       alert2.getDialogPane().setExpandableContent((Node)expContent);
/*      */       
/* 1048 */       alert2.showAndWait();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectServerBoxChanged(ActionEvent event) {
/* 1060 */     int index = this.selectServerBox.getSelectionModel().getSelectedIndex();
/* 1061 */     ServerEntry entry = this.servers.get(Integer.valueOf(index));
/* 1062 */     lServerPropertySheet = new ServerPropertySheet(entry);
/* 1063 */     buildOtherPropertyTab();
/* 1064 */     this.tab2ScrollPane.setContent((Node)localServerPropertySheet);
/* 1065 */     this.tab3ScrollPane.setContent((Node)lServerPropertySheet);
/*      */     
/* 1067 */     this.tab3ScrollPane.requestFocus();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void selectPlayerBoxChanged(ActionEvent event) {
/* 1078 */     if (!this.resettingPlayers) {
/*      */       
/* 1080 */       String name = ((String)this.selectPlayerComboBox.getSelectionModel().getSelectedItem()).toString();
/* 1081 */       logger.log(Level.INFO, "Selecting " + name);
/* 1082 */       PlayerData data = PlayerDBInterface.getPlayerData(name);
/* 1083 */       if (data != null) {
/*      */         
/* 1085 */         playerPropertySheet = new PlayerPropertySheet(data);
/* 1086 */         buildPlayerPropertyTab(false);
/* 1087 */         this.tab5ScrollPane.setContent((Node)playerPropertySheet);
/*      */         
/* 1089 */         this.tab5ScrollPane.requestFocus();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @FXML
/*      */   void initialize() {
/* 1100 */     assert this.tab1ContentPane != null : "fx:id=\"tab1ContentPane\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1101 */     assert this.tab2ContentPane != null : "fx:id=\"tab2ContentPane\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1102 */     assert this.tab1ScrollPane != null : "fx:id=\"tab1ScrollPane\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1103 */     assert this.tab2ScrollPane != null : "fx:id=\"tab2ScrollPane\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1104 */     assert this.runningControls != null : "fx:id=\"runningControls\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1105 */     assert this.gameControls != null : "fx:id=\"gameControls\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1106 */     assert this.playersTab != null : "fx:id=\"playersTab\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1107 */     assert this.serverTravelTab != null : "fx:id=\"serverTravelTab\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1108 */     assert this.serverNeighborTab != null : "fx:id=\"serverNeighborTab\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1109 */     assert this.localServerTab != null : "fx:id=\"localServerTab\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1110 */     assert this.updateGameBtn != null : "fx:id=\"updateGameBtn\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1111 */     assert this.updateRequiredLabel != null : "fx:id=\"updateRequiredLabel\" was not injected: check your FXML file 'WurmServerGui.fxml'.";
/* 1112 */     assert this.chkAutoNetwork != null : "fx:id=\"chkAutoNetwork\" was not injected: check your FXML file 'WurmServerGui.fxml'";
/*      */     
/* 1114 */     if (!Folders.loadDist()) {
/*      */       
/* 1116 */       logger.warning("Unable to load 'dist' folder, please run Steam validation");
/* 1117 */       showErrorDialog("Corrupt install", "Please run Steam validation to correct issues.", false);
/*      */       return;
/*      */     } 
/* 1120 */     if (!Folders.loadPresets())
/* 1121 */       logger.warning("Failed to load all presets"); 
/* 1122 */     if (!Folders.loadGames()) {
/* 1123 */       logger.warning("Failed to load game folders");
/*      */     }
/* 1125 */     DbConnector.setUseSqlite(true);
/*      */ 
/*      */     
/* 1128 */     if (Folders.getGameFolders().size() == 0) {
/*      */       
/* 1130 */       if (DistEntity.Creative.existsIn(Folders.getDist().getPath())) {
/*      */         
/* 1132 */         GameFolder creative = new GameFolder(Folders.getGamesPath().resolve("Creative"));
/* 1133 */         creative.create();
/* 1134 */         PresetFolder creativePreset = PresetFolder.fromPath(Folders.getDist().getPath().resolve(DistEntity.Creative.filename()));
/* 1135 */         if (creativePreset != null && 
/* 1136 */           creativePreset.copyTo(creative.getPath()))
/* 1137 */           Folders.addGame(creative); 
/*      */       } 
/* 1139 */       if (DistEntity.Adventure.existsIn(Folders.getDist().getPath())) {
/*      */         
/* 1141 */         GameFolder adventure = new GameFolder(Folders.getGamesPath().resolve("Adventure"));
/* 1142 */         adventure.create();
/* 1143 */         PresetFolder adventurePreset = PresetFolder.fromPath(Folders.getDist().getPath().resolve(DistEntity.Adventure.filename()));
/* 1144 */         if (adventurePreset != null && 
/* 1145 */           adventurePreset.copyTo(adventure.getPath())) {
/* 1146 */           Folders.addGame(adventure);
/*      */         }
/*      */       } 
/*      */     } 
/* 1150 */     if (Folders.getCurrent() != null) {
/*      */       
/* 1152 */       setCurrent(Folders.getCurrent());
/*      */     }
/*      */     else {
/*      */       
/* 1156 */       setCurrent(Folders.getGameFolders().get(0));
/*      */     } 
/*      */     
/* 1159 */     this.nullServer = new ServerEntry();
/* 1160 */     this.nullServer.isCreating = true;
/* 1161 */     buildDatabaseComboBox(false);
/*      */ 
/*      */     
/* 1164 */     if (!Objects.equals(Constants.dbHost, "localhost")) {
/* 1165 */       buildSelectServerBox(false);
/*      */     }
/* 1167 */     lServerPropertySheet = new ServerPropertySheet(this.servers.get(Integer.valueOf(0)));
/* 1168 */     buildOtherPropertyTab();
/* 1169 */     buildLocalPropertyTab();
/*      */     
/* 1171 */     this.tab2ScrollPane.setContent((Node)localServerPropertySheet);
/* 1172 */     this.tab3ScrollPane.setContent((Node)lServerPropertySheet);
/*      */ 
/*      */     
/* 1175 */     this.shutdownButton.setDisable(true);
/*      */ 
/*      */     
/* 1178 */     this.rootTabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> checkIfWeWantToSaveTab(oldValue.intValue(), newValue.intValue()));
/*      */     
/* 1180 */     buildSelectPlayerBox(false);
/* 1181 */     if (Folders.getCurrent() != null) {
/* 1182 */       setStageTitle("(" + Folders.getCurrent().getName() + ") Wurm Unlimited Server v" + SteamVersion.getCurrentVersion().toString());
/*      */     } else {
/* 1184 */       setStageTitle("Wurm Unlimited Server v" + SteamVersion.getCurrentVersion().toString());
/* 1185 */     }  String gameErrors = Folders.getCurrent().getError();
/* 1186 */     if (!gameErrors.isEmpty())
/*      */     {
/* 1188 */       showErrorDialog("Selected game is corrupt", gameErrors, true);
/*      */     }
/*      */     
/* 1191 */     this.chkAutoNetwork.setSelected(ServerProperties.getBoolean("AUTO_NETWORKING", Constants.enableAutoNetworking));
/* 1192 */     this.chkAutoNetwork.selectedProperty().addListener((observable, oldValue, newValue) -> {
/*      */           if (oldValue == newValue || newValue.booleanValue() == ServerProperties.getBoolean("AUTO_NETWORKING", Constants.enableAutoNetworking)) {
/*      */             return;
/*      */           }
/*      */           ServerProperties.setValue("AUTO_NETWORKING", Boolean.toString(newValue.booleanValue()));
/*      */           ServerProperties.checkProperties();
/*      */         });
/*      */   }
/*      */   
/*      */   private Port checkAndShowPortErrors(Port port) {
/* 1202 */     if ((port.getExceptions()).length == 0) {
/* 1203 */       return port;
/*      */     }
/*      */ 
/*      */     
/* 1207 */     String msg = "Error setting up " + port.getDescription() + " " + port.getPorts().toString() + "\n" + String.join("\n", (CharSequence[])port.getExceptions());
/* 1208 */     logger.warning(msg);
/* 1209 */     showErrorDialog("Network Configuration Error", msg, true);
/* 1210 */     return port;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean setupExternalNetwork(ServerEntry serverEntry) {
/* 1215 */     boolean autoPF = ServerProperties.getBoolean("ENABLE_PNP_PORT_FORWARD", Constants.enablePnpPortForward);
/* 1216 */     boolean autoNet = ServerProperties.getBoolean("AUTO_NETWORKING", Constants.enableAutoNetworking);
/* 1217 */     AddressHelper addressHelper = new AddressHelper();
/* 1218 */     InetAddress externalAddress = null;
/*      */     
/*      */     try {
/* 1221 */       externalAddress = InetAddress.getByName(serverEntry.EXTERNALIP);
/*      */     }
/* 1223 */     catch (UnknownHostException e) {
/*      */       
/* 1225 */       if (!autoNet) {
/*      */         
/* 1227 */         showErrorDialog("Exception", "Unknown IP address: " + e.getMessage() + ". Try enabling Auto Networking.", true);
/* 1228 */         return false;
/*      */       } 
/*      */     } 
/* 1231 */     if (externalAddress != null)
/*      */     {
/* 1233 */       if (!addressHelper.validateAddress(externalAddress)) {
/*      */         
/* 1235 */         if (!autoNet) {
/*      */           
/* 1237 */           String msg = "The value for External IP is invalid:\n" + String.join("\n", (CharSequence[])addressHelper.getExceptions());
/* 1238 */           logger.warning(msg);
/* 1239 */           showErrorDialog("Network Configuration Error", msg, true);
/* 1240 */           return false;
/*      */         } 
/* 1242 */         externalAddress = null;
/*      */       } 
/*      */     }
/*      */     
/* 1246 */     if (externalAddress == null)
/* 1247 */       externalAddress = AddressHelper.getFirstValidAddress(); 
/* 1248 */     if (externalAddress == null) {
/* 1249 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1253 */     Port steamAuth = (new Port(externalAddress, Protocol.Both)).setDescription("Steam communication").addPort(8766).toMap(autoPF);
/*      */ 
/*      */     
/* 1256 */     Port wuPort = (new Port(externalAddress, Protocol.TCP)).setDescription("Wurm Unlimited").toMap(autoPF);
/*      */ 
/*      */     
/* 1259 */     Port query = (new Port(externalAddress, Protocol.UDP)).setDescription("Steam Query port").toMap(autoPF);
/*      */     
/* 1261 */     wuPort.setFavoredPort(Integer.parseInt(serverEntry.EXTERNALPORT));
/* 1262 */     query.setFavoredPort(SteamHandler.steamQueryPort);
/*      */     
/* 1264 */     if (autoNet) {
/*      */       
/* 1266 */       wuPort.addPortRange(3000, 4000);
/* 1267 */       query.addPortRange(27016, 27030);
/*      */     } 
/* 1269 */     if (autoPF)
/* 1270 */       UPNPService.initialize(); 
/* 1271 */     steamAuth = checkAndShowPortErrors(PortHelper.assignPort(steamAuth));
/* 1272 */     wuPort = checkAndShowPortErrors(PortHelper.assignPort(wuPort));
/* 1273 */     query = checkAndShowPortErrors(PortHelper.assignPort(query));
/* 1274 */     if ((steamAuth.getExceptions()).length > 0 || (wuPort.getExceptions()).length > 0 || (query.getExceptions()).length > 0)
/* 1275 */       return false; 
/* 1276 */     SteamHandler.steamQueryPort = (short)query.getAssignedPort();
/* 1277 */     ServerProperties.setValue("STEAMQUERYPORT", String.valueOf(query.getAssignedPort()));
/* 1278 */     serverEntry.EXTERNALIP = externalAddress.getHostAddress();
/* 1279 */     serverEntry.EXTERNALPORT = String.valueOf(wuPort.getAssignedPort());
/* 1280 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean setupInternalNetwork(ServerEntry serverEntry) {
/* 1285 */     boolean autoNet = ServerProperties.getBoolean("AUTO_NETWORKING", Constants.enableAutoNetworking);
/* 1286 */     AddressHelper addressHelper = new AddressHelper();
/* 1287 */     addressHelper.setAddressValidators(new IAddressValidator[] { (IAddressValidator)new IP4Validator() });
/* 1288 */     InetAddress internalAddress = null;
/* 1289 */     if (!serverEntry.INTRASERVERADDRESS.isEmpty()) {
/*      */       
/*      */       try {
/*      */         
/* 1293 */         internalAddress = InetAddress.getByName(serverEntry.INTRASERVERADDRESS);
/*      */       }
/* 1295 */       catch (UnknownHostException e) {
/*      */         
/* 1297 */         if (!autoNet) {
/*      */           
/* 1299 */           showErrorDialog("Exception", "Unknown IP address: " + e.getMessage() + ". Try enabling Auto Networking.", true);
/* 1300 */           return false;
/*      */         } 
/*      */       } 
/*      */     }
/* 1304 */     if (internalAddress != null)
/*      */     {
/* 1306 */       if (internalAddress != InetAddress.getLoopbackAddress() && !addressHelper.validateAddress(internalAddress)) {
/*      */         
/* 1308 */         if (!autoNet) {
/*      */           
/* 1310 */           String msg = "The value for Internal Address is invalid:\n" + String.join("\n", (CharSequence[])addressHelper.getExceptions());
/* 1311 */           logger.warning(msg);
/* 1312 */           showErrorDialog("Network Configuration Error", msg, true);
/* 1313 */           return false;
/*      */         } 
/* 1315 */         internalAddress = null;
/*      */       } 
/*      */     }
/* 1318 */     if (internalAddress == null) {
/* 1319 */       internalAddress = InetAddress.getLoopbackAddress();
/*      */     }
/* 1321 */     Port internalPort = (new Port(internalAddress, Protocol.TCP)).setDescription("Wurm Unlimited Internal");
/* 1322 */     Port rmiPort = (new Port(internalAddress, Protocol.TCP)).setDescription("WU RMI Port");
/* 1323 */     Port rmiRegistrationPort = (new Port(internalAddress, Protocol.TCP)).setDescription("WU RMI Registration");
/*      */     
/* 1325 */     if (!autoNet) {
/*      */       
/* 1327 */       internalPort.addPort(Integer.parseInt(serverEntry.INTRASERVERPORT));
/* 1328 */       rmiPort.addPort(serverEntry.RMI_PORT);
/* 1329 */       rmiRegistrationPort.addPort(serverEntry.REGISTRATION_PORT);
/*      */     }
/*      */     else {
/*      */       
/* 1333 */       internalPort.addPortRange(40000, 41000);
/* 1334 */       rmiPort.addPortRange(7120, 7220);
/* 1335 */       rmiRegistrationPort.addPortRange(7221, 7320);
/*      */     } 
/*      */     
/* 1338 */     internalPort = checkAndShowPortErrors(PortHelper.assignPort(internalPort));
/* 1339 */     if (Constants.useIncomingRMI) {
/*      */       
/* 1341 */       rmiPort = checkAndShowPortErrors(PortHelper.assignPort(rmiPort));
/* 1342 */       rmiRegistrationPort = checkAndShowPortErrors(PortHelper.assignPort(rmiRegistrationPort));
/*      */     } 
/*      */     
/* 1345 */     if ((internalPort.getExceptions()).length > 0)
/* 1346 */       return false; 
/* 1347 */     if (Constants.useIncomingRMI && ((rmiPort.getExceptions()).length > 0 || (rmiRegistrationPort.getExceptions()).length > 0)) {
/* 1348 */       return false;
/*      */     }
/* 1350 */     serverEntry.INTRASERVERADDRESS = internalAddress.getHostAddress();
/* 1351 */     serverEntry.INTRASERVERPORT = String.valueOf(internalPort.getAssignedPort());
/* 1352 */     serverEntry.RMI_PORT = rmiPort.getAssignedPort();
/* 1353 */     serverEntry.REGISTRATION_PORT = rmiRegistrationPort.getAssignedPort();
/* 1354 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void startGame(boolean offline) {
/* 1365 */     if (this.launcher != null) {
/*      */       
/* 1367 */       if (this.launcher.wasStarted()) {
/*      */         
/* 1369 */         showErrorDialog("Failed to start", "The server has already been started. You need to restart this gui.", false);
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */     } else {
/* 1376 */       this.launcher = new ServerLauncher();
/*      */     } 
/*      */     
/* 1379 */     if (!offline) {
/*      */       
/* 1381 */       if (!setupInternalNetwork(Servers.localServer))
/*      */         return; 
/* 1383 */       if (!setupExternalNetwork(Servers.localServer)) {
/*      */         return;
/*      */       }
/*      */     } 
/* 1387 */     Servers.localServer.saveNewGui(Servers.localServer.id);
/*      */     
/* 1389 */     if (this.launcher.getServer() != null) {
/* 1390 */       this.launcher.getServer().setExternalIp();
/*      */     }
/*      */     
/*      */     try {
/* 1394 */       LocateRegistry.createRegistry(Servers.localServer.REGISTRATION_PORT);
/*      */       
/* 1396 */       ServerProperties.setValue("ADMINPASSWORD", adminPassword);
/* 1397 */       this.launcher.runServer(true, offline);
/*      */       
/* 1399 */       this.gameControls.setVisible(false);
/* 1400 */       this.startBtn.setDisable(true);
/* 1401 */       this.startOfflineBtn.setDisable(true);
/* 1402 */       this.databaseComboBox.setDisable(true);
/* 1403 */       this.chkAutoNetwork.setVisible(false);
/* 1404 */       this.shutdownButton.setDisable(false);
/* 1405 */       this.runningControls.setVisible(true);
/*      */ 
/*      */       
/* 1408 */       localServerPropertySheet.setReadOnly();
/* 1409 */       this.localServerText.setText("Can't change values when the server is running");
/*      */     }
/* 1411 */     catch (IOException iox) {
/*      */       
/* 1413 */       showErrorDialog("Failed to start", "This is the message received when attempting to start the server: " + iox
/* 1414 */           .getMessage(), true);
/*      */     }
/*      */     finally {
/*      */       
/* 1418 */       System.out.println("\n==================================================================\n");
/* 1419 */       System.out.println("Wurm Server launcher finished at " + new Date());
/* 1420 */       System.out.println("\n==================================================================\n");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void startDB(String dbName) {
/* 1432 */     if (!Folders.loadDist()) {
/*      */       return;
/*      */     }
/* 1435 */     GameFolder folder = GameFolder.fromPath(Paths.get(dbName, new String[0]));
/* 1436 */     setCurrentFolder(folder);
/* 1437 */     if (folder == null) {
/*      */       
/* 1439 */       logger.warning("Null game folder");
/*      */       
/*      */       return;
/*      */     } 
/* 1443 */     String fileErr = folder.getError();
/* 1444 */     if (!fileErr.isEmpty()) {
/*      */       
/* 1446 */       logger.warning(fileErr);
/*      */       return;
/*      */     } 
/* 1449 */     if (!folder.setCurrent(true)) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/* 1454 */       DbConnector.closeAll();
/* 1455 */       ServerDirInfo.setPath(folder.getPath());
/*      */       
/* 1457 */       Constants.load();
/* 1458 */       Constants.dbHost = dbName;
/* 1459 */       Constants.dbPort = "";
/* 1460 */       DbConnector.closeAll();
/* 1461 */       DbConnector.initialize();
/* 1462 */       if (Constants.dbAutoMigrate) {
/*      */         
/* 1464 */         DbConnector.performMigrations();
/*      */       }
/* 1466 */       else if (DbConnector.hasPendingMigrations()) {
/*      */         
/* 1468 */         logger.warning("Pending migrations found but auto-migration disabled.");
/*      */       } 
/*      */       
/* 1471 */       initServer(dbName);
/*      */     }
/* 1473 */     catch (Exception ex) {
/*      */       
/* 1475 */       ex.printStackTrace();
/*      */ 
/*      */       
/*      */       try {
/* 1479 */         folder.setCurrent(false);
/* 1480 */         DbConnector.closeAll();
/*      */         
/*      */         return;
/* 1483 */       } catch (Exception ex2) {
/*      */         
/* 1485 */         ex2.printStackTrace();
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1490 */     ServerLauncher launcher = new ServerLauncher();
/*      */ 
/*      */     
/*      */     try {
/* 1494 */       LocateRegistry.createRegistry(Servers.localServer.REGISTRATION_PORT);
/* 1495 */       ServerProperties.setValue("ADMINPASSWORD", adminPassword);
/* 1496 */       launcher.runServer(true, false);
/*      */       
/* 1498 */       (new Thread((Runnable)new CommandReader(launcher.getServer(), System.in), "Console Command Reader")).start();
/*      */     }
/* 1500 */     catch (IOException iox) {
/*      */       
/* 1502 */       iox.printStackTrace();
/*      */     }
/*      */     finally {
/*      */       
/* 1506 */       System.out.println("\n==================================================================\n");
/* 1507 */       System.out.println("Wurm Server launcher finished at " + new Date());
/* 1508 */       System.out.println("\n==================================================================\n");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void buildSelectNeighbourBoxes() {
/* 1515 */     this.selectNeighbourBoxNorth.getItems().clear();
/* 1516 */     this.selectNeighbourBoxEast.getItems().clear();
/* 1517 */     this.selectNeighbourBoxWest.getItems().clear();
/* 1518 */     this.selectNeighbourBoxSouth.getItems().clear();
/* 1519 */     int northIndex = -1;
/* 1520 */     int southIndex = -1;
/* 1521 */     int westIndex = -1;
/* 1522 */     int eastIndex = -1;
/*      */     
/* 1524 */     this.selectionBoxServers.clear();
/* 1525 */     this.selectNeighbourBoxNorth.getItems().add("None");
/* 1526 */     this.selectNeighbourBoxNorth.getSelectionModel().select(Integer.valueOf(0).intValue());
/* 1527 */     this.selectNeighbourBoxEast.getItems().add("None");
/* 1528 */     this.selectNeighbourBoxEast.getSelectionModel().select(Integer.valueOf(0).intValue());
/* 1529 */     this.selectNeighbourBoxWest.getItems().add("None");
/* 1530 */     this.selectNeighbourBoxWest.getSelectionModel().select(Integer.valueOf(0).intValue());
/* 1531 */     this.selectNeighbourBoxSouth.getItems().add("None");
/* 1532 */     this.selectNeighbourBoxSouth.getSelectionModel().select(Integer.valueOf(0).intValue());
/*      */ 
/*      */     
/* 1535 */     int index = 0;
/* 1536 */     this.selectionBoxServers.put(Integer.valueOf(index++), this.nullServer);
/*      */     
/* 1538 */     List<ServerEntry> entries = Arrays.asList(Servers.getAllServers());
/* 1539 */     entries.sort(new Comparator<ServerEntry>()
/*      */         {
/*      */           
/*      */           public int compare(ServerEntry s1, ServerEntry s2)
/*      */           {
/* 1544 */             return s1.getName().compareToIgnoreCase(s2.getName());
/*      */           }
/*      */         });
/* 1547 */     for (ServerEntry entry : entries) {
/*      */       
/* 1549 */       if (!entry.isLocal) {
/*      */         
/* 1551 */         this.selectNeighbourBoxNorth.getItems().add(entry.getName());
/* 1552 */         if (Servers.localServer.serverNorth == entry)
/* 1553 */           northIndex = index; 
/* 1554 */         this.selectNeighbourBoxEast.getItems().add(entry.getName());
/* 1555 */         if (Servers.localServer.serverEast == entry)
/* 1556 */           eastIndex = index; 
/* 1557 */         this.selectNeighbourBoxWest.getItems().add(entry.getName());
/* 1558 */         if (Servers.localServer.serverWest == entry)
/* 1559 */           westIndex = index; 
/* 1560 */         this.selectNeighbourBoxSouth.getItems().add(entry.getName());
/* 1561 */         if (Servers.localServer.serverSouth == entry)
/* 1562 */           southIndex = index; 
/* 1563 */         this.selectionBoxServers.put(Integer.valueOf(index), entry);
/* 1564 */         index++;
/*      */       } 
/*      */     } 
/* 1567 */     if (northIndex >= 0)
/* 1568 */       this.selectNeighbourBoxNorth.getSelectionModel().select(northIndex); 
/* 1569 */     if (westIndex >= 0)
/* 1570 */       this.selectNeighbourBoxWest.getSelectionModel().select(westIndex); 
/* 1571 */     if (eastIndex >= 0)
/* 1572 */       this.selectNeighbourBoxEast.getSelectionModel().select(eastIndex); 
/* 1573 */     if (southIndex >= 0)
/* 1574 */       this.selectNeighbourBoxSouth.getSelectionModel().select(southIndex); 
/* 1575 */     this.saveNeighboursButton.setDisable(true);
/*      */   }
/*      */ 
/*      */   
/*      */   boolean resettingPlayers = false;
/*      */   
/*      */   public final void buildSelectPlayerBox(boolean reload) {
/* 1582 */     this.resettingPlayers = reload;
/* 1583 */     this.selectPlayerComboBox.getItems().clear();
/*      */     
/* 1585 */     PlayerDBInterface.loadAllData();
/* 1586 */     PlayerDBInterface.loadAllPositionData();
/*      */     
/* 1588 */     for (PlayerData entry : PlayerDBInterface.getAllData())
/*      */     {
/* 1590 */       this.selectPlayerComboBox.getItems().add(entry.getName());
/*      */     }
/*      */     
/* 1593 */     this.selectPlayerComboBox.getItems().sort(new Comparator<String>()
/*      */         {
/*      */           
/*      */           public int compare(String s1, String s2)
/*      */           {
/* 1598 */             return s1.compareToIgnoreCase(s2);
/*      */           }
/*      */         });
/* 1601 */     this.resettingPlayers = false;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void buildSelectServerBox(boolean reload) {
/* 1606 */     this.selectServerBox.getSelectionModel().clearSelection();
/* 1607 */     this.selectServerBox.getItems().clear();
/* 1608 */     Servers.loadAllServers(reload);
/* 1609 */     this.servers.clear();
/* 1610 */     int index = 0;
/* 1611 */     short newRand = ServerPropertySheet.getNewServerId();
/* 1612 */     List<ServerEntry> entries = Arrays.asList(Servers.getAllServers());
/* 1613 */     entries.sort(new Comparator<ServerEntry>()
/*      */         {
/*      */           
/*      */           public int compare(ServerEntry s1, ServerEntry s2)
/*      */           {
/* 1618 */             return s1.getName().compareToIgnoreCase(s2.getName());
/*      */           }
/*      */         });
/* 1621 */     for (ServerEntry entry : entries) {
/*      */       
/* 1623 */       if (entry.isLocal) {
/*      */         
/* 1625 */         localServerPropertySheet = new ServerPropertySheet(entry);
/*      */         
/*      */         continue;
/*      */       } 
/* 1629 */       this.selectServerBox.getItems().add(entry.getName());
/* 1630 */       this.servers.put(Integer.valueOf(index), entry);
/* 1631 */       index++;
/*      */     } 
/*      */ 
/*      */     
/* 1635 */     if (newRand > 0) {
/*      */       
/* 1637 */       this.selectServerBox.getItems().add("Create new ...");
/* 1638 */       ServerEntry newServer = new ServerEntry();
/* 1639 */       newServer.isCreating = true;
/* 1640 */       newServer.name = "The New Server";
/* 1641 */       newServer.id = newRand;
/* 1642 */       if (Servers.localServer != null) {
/*      */         
/* 1644 */         newServer.EXTERNALIP = Servers.localServer.EXTERNALIP;
/* 1645 */         newServer.EXTERNALPORT = Servers.localServer.EXTERNALPORT;
/* 1646 */         newServer.INTRASERVERADDRESS = Servers.localServer.INTRASERVERADDRESS;
/* 1647 */         newServer.INTRASERVERPORT = Servers.localServer.INTRASERVERPORT;
/* 1648 */         newServer.INTRASERVERPASSWORD = Servers.localServer.INTRASERVERPASSWORD;
/* 1649 */         newServer.RMI_PORT = Servers.localServer.RMI_PORT;
/* 1650 */         newServer.REGISTRATION_PORT = Servers.localServer.REGISTRATION_PORT;
/*      */       }
/*      */       else {
/*      */         
/* 1654 */         newServer.EXTERNALPORT = "3724";
/* 1655 */         newServer.INTRASERVERPORT = "48020";
/* 1656 */         newServer.RMI_PORT = 7220;
/* 1657 */         newServer.REGISTRATION_PORT = 7221;
/* 1658 */         newServer.isLocal = true;
/* 1659 */         newServer.LOGINSERVER = true;
/* 1660 */         newServer.SPAWNPOINTJENNX = 200;
/* 1661 */         newServer.SPAWNPOINTJENNY = 200;
/* 1662 */         Servers.localServer = newServer;
/* 1663 */         localServerPropertySheet = new ServerPropertySheet(newServer);
/*      */       } 
/* 1665 */       this.servers.put(Integer.valueOf(index), newServer);
/*      */     } 
/* 1667 */     this.selectServerBox.getSelectionModel().select(Integer.valueOf(0).intValue());
/* 1668 */     buildSelectNeighbourBoxes();
/*      */   }
/*      */ 
/*      */   
/*      */   final void buildLocalPropertyTab() {
/* 1673 */     List<Node> nodes = new ArrayList<>((Collection<? extends Node>)localServerPropertySheet.getChildren());
/* 1674 */     nodes.remove(this.saveServerButtonLocal);
/* 1675 */     nodes.remove(this.localServerText);
/* 1676 */     localServerPropertySheet.getChildren().clear();
/* 1677 */     localServerPropertySheet.getChildren().add(this.saveServerButtonLocal);
/* 1678 */     localServerPropertySheet.getChildren().add(this.localServerText);
/* 1679 */     localServerPropertySheet.getChildren().addAll(nodes);
/*      */   }
/*      */ 
/*      */   
/*      */   final void buildOtherPropertyTab() {
/* 1684 */     List<Node> nodes = new ArrayList<>((Collection<? extends Node>)lServerPropertySheet.getChildren());
/* 1685 */     nodes.remove(this.selectServerBox);
/* 1686 */     lServerPropertySheet.getChildren().clear();
/* 1687 */     lServerPropertySheet.getChildren().add(this.selectServerBox);
/* 1688 */     HBox hbox = new HBox();
/* 1689 */     hbox.getChildren().add(this.saveServerButton);
/* 1690 */     hbox.getChildren().add(this.deleteServerBtn);
/* 1691 */     lServerPropertySheet.getChildren().add(hbox);
/* 1692 */     lServerPropertySheet.getChildren().addAll(nodes);
/*      */   }
/*      */ 
/*      */   
/*      */   final void buildPlayerPropertyTab(boolean clear) {
/* 1697 */     if (!clear || playerPropertySheet != null) {
/*      */       
/* 1699 */       List<Node> nodes = new ArrayList<>((Collection<? extends Node>)playerPropertySheet.getChildren());
/* 1700 */       playerPropertySheet.getChildren().clear();
/* 1701 */       playerPropertySheet.getChildren().add(this.selectPlayerComboBox);
/* 1702 */       playerPropertySheet.getChildren().add(this.savePlayerBtn);
/* 1703 */       if (!clear) {
/* 1704 */         playerPropertySheet.getChildren().addAll(nodes);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean rebuilding = false;
/*      */   
/*      */   final void buildDatabaseComboBox(boolean changedDirectory) {
/* 1712 */     this.rebuilding = true;
/* 1713 */     this.databaseComboBox.getItems().clear();
/* 1714 */     for (GameFolder folder : Folders.getGameFolders())
/*      */     {
/* 1716 */       this.databaseComboBox.getItems().add(folder.getName());
/*      */     }
/* 1718 */     this.databaseComboBox.getItems().sort(new Comparator<String>()
/*      */         {
/*      */           
/*      */           public int compare(String s1, String s2)
/*      */           {
/* 1723 */             return s1.compareToIgnoreCase(s2);
/*      */           }
/*      */         });
/* 1726 */     if (Folders.getCurrent() != null) {
/*      */       
/* 1728 */       this.databaseComboBox.getSelectionModel().select(Folders.getCurrent().getName());
/* 1729 */       this.renameTxtfield.setText(Folders.getCurrent().getName());
/* 1730 */       this.copyTextField.setText(Folders.getCurrent().getName() + "Copy");
/* 1731 */       Constants.load();
/* 1732 */       Constants.dbHost = Folders.getCurrent().getName();
/* 1733 */       Constants.dbPort = "";
/* 1734 */       DbConnector.closeAll();
/* 1735 */       DbConnector.initialize(true);
/* 1736 */       this.gameControls.setVisible(true);
/* 1737 */       if (DbConnector.hasPendingMigrations()) {
/*      */         
/* 1739 */         this.startBtn.setDisable(true);
/* 1740 */         this.startOfflineBtn.setDisable(true);
/* 1741 */         setDisableTabs(true);
/* 1742 */         this.chkAutoNetwork.setVisible(false);
/* 1743 */         this.updateRequiredLabel.setVisible(true);
/* 1744 */         this.updateGameBtn.setDisable(false);
/*      */       }
/* 1746 */       else if (Folders.getCurrent().getError().isEmpty()) {
/*      */         
/* 1748 */         this.startBtn.setDisable(false);
/* 1749 */         this.startOfflineBtn.setDisable(false);
/* 1750 */         setDisableTabs(false);
/* 1751 */         this.chkAutoNetwork.setVisible(true);
/* 1752 */         this.updateRequiredLabel.setVisible(false);
/* 1753 */         this.updateGameBtn.setDisable(true);
/*      */       } 
/*      */       
/* 1756 */       if (changedDirectory) {
/*      */         
/* 1758 */         buildSelectServerBox(true);
/*      */         
/* 1760 */         lServerPropertySheet = new ServerPropertySheet(this.servers.get(Integer.valueOf(0)));
/* 1761 */         buildOtherPropertyTab();
/* 1762 */         buildLocalPropertyTab();
/*      */         
/* 1764 */         this.tab2ScrollPane.setContent((Node)localServerPropertySheet);
/* 1765 */         this.tab3ScrollPane.setContent((Node)lServerPropertySheet);
/* 1766 */         buildSelectPlayerBox(true);
/* 1767 */         buildPlayerPropertyTab(true);
/*      */       } 
/*      */     } 
/* 1770 */     this.rebuilding = false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void checkIfWeWantToSaveTab(int oldvalue, int newValue) {
/* 1775 */     if (oldvalue != newValue)
/*      */     {
/*      */       
/* 1778 */       if (oldvalue == 1)
/*      */       {
/* 1780 */         if (localServerPropertySheet.haveChanges())
/*      */         {
/* 1782 */           localServerPropertySheet.AskIfSave();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStage(Stage primaryStage) {
/* 1790 */     this.primaryStage = primaryStage;
/* 1791 */     if (Folders.getCurrent() != null) {
/* 1792 */       setStageTitle("(" + Folders.getCurrent().getName() + ") Wurm Unlimited Server v" + SteamVersion.getCurrentVersion().toString());
/*      */     } else {
/* 1794 */       setStageTitle("Wurm Unlimited Server v" + SteamVersion.getCurrentVersion().toString());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void setStageTitle(String title) {
/* 1799 */     if (this.primaryStage != null) {
/* 1800 */       this.primaryStage.setTitle(title);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean shutdown() {
/* 1805 */     if (this.launcher != null && this.launcher.wasStarted()) {
/*      */       
/* 1807 */       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
/* 1808 */       alert.setTitle("Stop Server");
/* 1809 */       alert.setHeaderText("This will shut down the server.");
/* 1810 */       alert.setContentText("Are you really sure you want to do this?");
/*      */       
/* 1812 */       Optional<ButtonType> result = alert.showAndWait();
/* 1813 */       if (result.isPresent() && result.get() == ButtonType.OK) {
/*      */         
/* 1815 */         System.out.println("The server is shutting down");
/* 1816 */         this.launcher.getServer().shutDown();
/*      */       } else {
/*      */         
/* 1819 */         return false;
/*      */       } 
/* 1821 */     }  return true;
/*      */   }
/*      */ 
/*      */   
/*      */   void setDisableTabs(boolean disable) {
/* 1826 */     this.playersTab.setDisable(disable);
/* 1827 */     this.serverNeighborTab.setDisable(disable);
/* 1828 */     this.localServerTab.setDisable(disable);
/* 1829 */     this.serverTravelTab.setDisable(disable);
/*      */   }
/*      */   
/*      */   private static interface ArgumentApplication {
/*      */     void applyArgument(String param1String);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\gui\WurmServerGuiController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
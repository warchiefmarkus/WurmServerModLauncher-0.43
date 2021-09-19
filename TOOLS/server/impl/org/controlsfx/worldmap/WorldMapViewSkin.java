/*     */ package impl.org.controlsfx.worldmap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import javafx.beans.Observable;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ListChangeListener;
/*     */ import javafx.collections.MapChangeListener;
/*     */ import javafx.collections.ObservableMap;
/*     */ import javafx.collections.WeakListChangeListener;
/*     */ import javafx.css.PseudoClass;
/*     */ import javafx.event.EventTarget;
/*     */ import javafx.geometry.Point2D;
/*     */ import javafx.scene.Group;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.control.SkinBase;
/*     */ import javafx.scene.input.MouseButton;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.input.ScrollEvent;
/*     */ import javafx.scene.input.ZoomEvent;
/*     */ import javafx.scene.layout.BorderPane;
/*     */ import javafx.scene.layout.Pane;
/*     */ import javafx.scene.shape.Rectangle;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.WorldMapView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldMapViewSkin
/*     */   extends SkinBase<WorldMapView>
/*     */ {
/*  52 */   private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
/*     */   
/*     */   private static final String DEFAULT_STYLE_LOCATION = "location";
/*     */   
/*     */   private static final String DEFAULT_STYLE_COUNTRY = "country";
/*     */   private static final double PREFERRED_WIDTH = 1009.0D;
/*     */   private static final double PREFERRED_HEIGHT = 665.0D;
/*  59 */   private static double MAP_OFFSET_X = -28.756500000000003D;
/*  60 */   private static double MAP_OFFSET_Y = 129.675D;
/*     */   
/*  62 */   private final Map<WorldMapView.Country, List<? extends String>> countryPathMap = new HashMap<>();
/*  63 */   private final Map<WorldMapView.Country, List<? extends WorldMapView.CountryView>> countryViewMap = new HashMap<>();
/*     */   private Pane countryPane; private Group group; private Group locationsGroup; protected ObservableMap<WorldMapView.Location, Node> locationMap; private double dragX; private double dragY; private final ListChangeListener<? super WorldMapView.Location> locationsListener;
/*     */   private final WeakListChangeListener weakLocationsListener;
/*     */   private final ListChangeListener<? super WorldMapView.Country> countrySelectionListener;
/*     */   private final WeakListChangeListener weakCountrySelectionListener;
/*     */   private final ListChangeListener<? super WorldMapView.Location> locationSelectionListener;
/*     */   private final WeakListChangeListener weakLocationSelectionListener;
/*     */   
/*  71 */   public WorldMapViewSkin(WorldMapView view) { super((Control)view);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     this.locationsListener = (change -> {
/*     */         while (change.next()) {
/*     */           if (change.wasAdded()) {
/*     */             change.getAddedSubList().forEach(());
/*     */             continue;
/*     */           } 
/*     */           if (change.wasRemoved())
/*     */             change.getRemoved().forEach(()); 
/*     */         } 
/*     */       });
/* 225 */     this.weakLocationsListener = new WeakListChangeListener(this.locationsListener);
/*     */ 
/*     */ 
/*     */     
/* 229 */     this.countrySelectionListener = (change -> {
/*     */         while (change.next()) {
/*     */           if (change.wasAdded()) {
/*     */             change.getAddedSubList().forEach(());
/*     */             
/*     */             continue;
/*     */           } 
/*     */           if (change.wasRemoved()) {
/*     */             change.getRemoved().forEach(());
/*     */           }
/*     */         } 
/*     */       });
/* 241 */     this.weakCountrySelectionListener = new WeakListChangeListener(this.countrySelectionListener);
/*     */ 
/*     */ 
/*     */     
/* 245 */     this.locationSelectionListener = (change -> {
/*     */         while (change.next()) {
/*     */           if (change.wasAdded()) {
/*     */             change.getAddedSubList().forEach(());
/*     */             
/*     */             continue;
/*     */           } 
/*     */           if (change.wasRemoved()) {
/*     */             change.getRemoved().forEach(());
/*     */           }
/*     */         } 
/*     */       });
/* 257 */     this.weakLocationSelectionListener = new WeakListChangeListener(this.locationSelectionListener); this.locationMap = FXCollections.observableHashMap(); this.group = new Group(); this.group.setManaged(false); this.group.setAutoSizeChildren(false); this.locationsGroup = new Group(); this.locationsGroup.setManaged(false); this.locationsGroup.visibleProperty().bind((ObservableValue)view.showLocationsProperty()); this.locationsGroup.setAutoSizeChildren(false); this.countryPane = new Pane(); this.countryPane.getChildren().add(this.group); view.getLocations().addListener(this.locationsListener); ListChangeListener<? super WorldMapView.Country> countriesListener = change -> buildView(); view.getCountries().addListener(countriesListener); this.locationMap.addListener(change -> { if (change.wasRemoved()) this.locationsGroup.getChildren().remove(change.getValueRemoved());  }); BorderPane borderPane = new BorderPane(); borderPane.setCenter((Node)this.countryPane); getChildren().add(borderPane); view.zoomFactorProperty().addListener(it -> view.requestLayout()); Properties mapData = loadData(); for (WorldMapView.Country country : WorldMapView.Country.values()) { String countryData = (String)mapData.get(country.name()); if (countryData == null) { System.out.println("Missing SVG path for country " + country.getLocale().getDisplayCountry() + " (" + country + ")"); } else { StringTokenizer st = new StringTokenizer(countryData, ";"); List<String> paths = new ArrayList<>(); while (st.hasMoreTokens())
/*     */           paths.add(st.nextToken());  this.countryPathMap.put(country, paths); }  }  buildView(); view.getSelectedCountries().addListener((ListChangeListener)this.weakCountrySelectionListener); view.selectedCountriesProperty().addListener(it -> view.getSelectedCountries().addListener((ListChangeListener)this.weakCountrySelectionListener)); view.getSelectedLocations().addListener((ListChangeListener)this.weakLocationSelectionListener); view.selectedLocationsProperty().addListener(it -> view.getSelectedLocations().addListener((ListChangeListener)this.weakLocationSelectionListener)); view.getLocations().addListener((ListChangeListener)this.weakLocationsListener); view.locationsProperty().addListener(it -> view.getLocations().addListener((ListChangeListener)this.weakLocationsListener)); view.getLocations().forEach(location -> addLocation(location)); view.addEventHandler(ScrollEvent.SCROLL, evt -> evt.consume()); view.addEventHandler(ZoomEvent.ZOOM, evt -> { double factor = evt.getZoomFactor(); view.setZoomFactor(view.getZoomFactor() * factor); evt.consume(); }); view.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> { this.dragX = evt.getX(); this.dragY = evt.getY(); }); view.addEventHandler(MouseEvent.MOUSE_DRAGGED, evt -> { double deltaX = evt.getX() - this.dragX; double deltaY = evt.getY() - this.dragY; this.group.setTranslateX(this.group.getTranslateX() + deltaX); this.group.setTranslateY(this.group.getTranslateY() + deltaY); this.dragX = evt.getX(); this.dragY = evt.getY(); }); view.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { if (evt.getClickCount() == 2) { view.setZoomFactor(1.0D); this.group.setTranslateX(0.0D); this.group.setTranslateY(0.0D); } else if (evt.getButton().equals(MouseButton.PRIMARY)) { EventTarget target = evt.getTarget(); if (target instanceof WorldMapView.CountryView) { WorldMapView.CountryView path = (WorldMapView.CountryView)target; WorldMapView.Country country = path.getCountry(); boolean wasSelected = view.getSelectedCountries().contains(country); if (view.getCountrySelectionMode().equals(WorldMapView.SelectionMode.SINGLE) || (!evt.isShortcutDown() && !evt.isShiftDown()))
/*     */                 view.getSelectedCountries().clear();  if (wasSelected) { view.getSelectedCountries().remove(country); } else { view.getSelectedCountries().add(country); }  } else if (target.equals(this.countryPane)) { view.getSelectedCountries().clear(); } else { for (WorldMapView.Location location : this.locationMap.keySet()) { Node node = (Node)this.locationMap.get(location); if (target.equals(node)) { boolean wasSelected = view.getSelectedLocations().contains(location); if (view.getLocationSelectionMode().equals(WorldMapView.SelectionMode.SINGLE) || (!evt.isShortcutDown() && !evt.isShiftDown()))
/* 260 */                     view.getSelectedLocations().clear();  if (wasSelected) { view.getSelectedLocations().remove(location); break; }  view.getSelectedLocations().add(location); break; }  }  }  }  }); Rectangle clip = new Rectangle(); clip.widthProperty().bind((ObservableValue)view.widthProperty()); clip.heightProperty().bind((ObservableValue)view.heightProperty()); view.setClip((Node)clip); view.countryViewFactoryProperty().addListener(it -> buildView()); view.locationViewFactoryProperty().addListener(it -> buildView()); } private Point2D getLocationCoordinates(WorldMapView.Location location) { double x = (location.getLongitude() + 180.0D) * 2.8027777777777776D + MAP_OFFSET_X;
/* 261 */     double y = 332.5D - 1009.0D * Math.log(Math.tan(0.7853981633974483D + Math.toRadians(location.getLatitude()) / 2.0D)) / 6.283185307179586D + MAP_OFFSET_Y;
/* 262 */     return new Point2D(x, y); }
/*     */ 
/*     */   
/*     */   private void addLocation(WorldMapView.Location location) {
/* 266 */     Point2D coordinates = getLocationCoordinates(location);
/* 267 */     Callback<WorldMapView.Location, Node> locationViewFactory = ((WorldMapView)getSkinnable()).getLocationViewFactory();
/* 268 */     Node view = (Node)locationViewFactory.call(location);
/* 269 */     if (view == null) {
/* 270 */       throw new IllegalArgumentException("location view factory returned NULL");
/*     */     }
/* 272 */     view.getStyleClass().add("location");
/* 273 */     view.setManaged(false);
/* 274 */     this.locationsGroup.getChildren().add(view);
/* 275 */     view.applyCss();
/* 276 */     view.resizeRelocate(coordinates.getX(), coordinates.getY(), view.prefWidth(-1.0D), view.prefHeight(-1.0D));
/* 277 */     this.locationMap.put(location, view);
/*     */   }
/*     */   
/*     */   private void removeLocation(WorldMapView.Location location) {
/* 281 */     this.locationMap.remove(location);
/*     */   }
/*     */   
/*     */   private void buildView() {
/* 285 */     this.group.getChildren().clear();
/* 286 */     this.locationsGroup.getChildren().clear();
/*     */     
/* 288 */     if (Double.compare(((WorldMapView)getSkinnable()).getPrefWidth(), 0.0D) <= 0 || Double.compare(((WorldMapView)getSkinnable()).getPrefHeight(), 0.0D) <= 0 || 
/* 289 */       Double.compare(((WorldMapView)getSkinnable()).getWidth(), 0.0D) <= 0 || Double.compare(((WorldMapView)getSkinnable()).getHeight(), 0.0D) <= 0) {
/* 290 */       if (((WorldMapView)getSkinnable()).getPrefWidth() > 0.0D && ((WorldMapView)getSkinnable()).getPrefHeight() > 0.0D) {
/* 291 */         ((WorldMapView)getSkinnable()).setPrefSize(((WorldMapView)getSkinnable()).getPrefWidth(), ((WorldMapView)getSkinnable()).getPrefHeight());
/*     */       } else {
/* 293 */         ((WorldMapView)getSkinnable()).setPrefSize(1009.0D, 665.0D);
/*     */       } 
/*     */     }
/*     */     
/* 297 */     Callback<WorldMapView.Country, WorldMapView.CountryView> factory = ((WorldMapView)getSkinnable()).getCountryViewFactory();
/* 298 */     for (WorldMapView.Country country : WorldMapView.Country.values()) {
/* 299 */       if (((WorldMapView)getSkinnable()).getCountries().isEmpty() || ((WorldMapView)getSkinnable()).getCountries().contains(country)) {
/* 300 */         List<WorldMapView.CountryView> countryViews = new ArrayList<>();
/* 301 */         for (String svgPath : this.countryPathMap.get(country)) {
/* 302 */           WorldMapView.CountryView view = (WorldMapView.CountryView)factory.call(country);
/* 303 */           if (view != null) {
/* 304 */             view.setContent(svgPath);
/* 305 */             view.getStyleClass().add(0, "country");
/* 306 */             this.group.getChildren().addAll((Object[])new Node[] { (Node)view });
/* 307 */             countryViews.add(view);
/*     */           } 
/*     */         } 
/* 310 */         this.countryViewMap.put(country, countryViews);
/*     */       } 
/*     */     } 
/*     */     
/* 314 */     for (WorldMapView.Location location : this.locationMap.keySet()) {
/* 315 */       Point2D coordinates = getLocationCoordinates(location);
/* 316 */       if (this.group.getLayoutBounds().contains(coordinates)) {
/* 317 */         this.locationsGroup.getChildren().add(this.locationMap.get(location));
/*     */       }
/*     */     } 
/*     */     
/* 321 */     this.group.getChildren().add(this.locationsGroup);
/*     */     
/* 323 */     ((WorldMapView)getSkinnable()).requestLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
/* 328 */     super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
/*     */     
/* 330 */     double prefWidth = this.group.prefWidth(-1.0D);
/* 331 */     double prefHeight = this.group.prefHeight(-1.0D);
/*     */     
/* 333 */     double scaleX = contentWidth / prefWidth;
/* 334 */     double scaleY = contentHeight / prefHeight;
/*     */     
/* 336 */     double scale = Math.min(scaleX, scaleY) * ((WorldMapView)getSkinnable()).getZoomFactor();
/*     */     
/* 338 */     this.group.setTranslateX(-this.group.getLayoutBounds().getMinX());
/* 339 */     this.group.setTranslateY(-this.group.getLayoutBounds().getMinY());
/*     */     
/* 341 */     this.group.setScaleX(scale);
/* 342 */     this.group.setScaleY(scale);
/*     */     
/* 344 */     this.group.setLayoutX((contentWidth - prefWidth) / 2.0D);
/* 345 */     this.group.setLayoutY((contentHeight - prefHeight) / 2.0D);
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
/*     */   protected Properties loadData() {
/* 362 */     Properties mapData = new Properties();
/*     */     try {
/* 364 */       mapData.load(WorldMapView.class.getResourceAsStream("worldmap-small.properties"));
/* 365 */     } catch (IOException e) {
/* 366 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 369 */     return mapData;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\worldmap\WorldMapViewSkin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
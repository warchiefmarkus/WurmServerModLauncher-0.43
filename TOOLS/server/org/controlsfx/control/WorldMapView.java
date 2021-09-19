/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.worldmap.WorldMapViewSkin;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.ListProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleListProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.collections.FXCollections;
/*     */ import javafx.collections.ObservableList;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.control.Skin;
/*     */ import javafx.scene.control.Tooltip;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.scene.shape.Circle;
/*     */ import javafx.scene.shape.SVGPath;
/*     */ import javafx.util.Callback;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldMapView
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private static final String DEFAULT_STYLE_CLASS = "world-map";
/*  87 */   private Tooltip tooltip = new Tooltip();
/*     */ 
/*     */   
/*     */   private final ObjectProperty<SelectionMode> countrySelectionMode;
/*     */ 
/*     */   
/*     */   private final ObjectProperty<SelectionMode> locationSelectionMode;
/*     */ 
/*     */   
/*     */   private final DoubleProperty zoomFactor;
/*     */ 
/*     */   
/*     */   private final ListProperty<Country> selectedCountries;
/*     */ 
/*     */   
/*     */   private final ListProperty<Location> selectedLocations;
/*     */   
/*     */   private final ListProperty<Country> countries;
/*     */   
/*     */   private final ListProperty<Location> locations;
/*     */   
/*     */   private final BooleanProperty showLocations;
/*     */   
/*     */   private final ObjectProperty<Callback<Location, Node>> locationViewFactory;
/*     */   
/*     */   private final ObjectProperty<Callback<Country, CountryView>> countryViewFactory;
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/* 116 */     return (Skin<?>)new WorldMapViewSkin(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/* 121 */     return getUserAgentStylesheet(WorldMapView.class, "world.css");
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
/*     */   public enum SelectionMode
/*     */   {
/* 138 */     SINGLE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     MULTIPLE; } public final ObjectProperty<SelectionMode> countrySelectionModeProperty() { return this.countrySelectionMode; } public final SelectionMode getCountrySelectionMode() { return (SelectionMode)this.countrySelectionMode.get(); } public final void setCountrySelectionMode(SelectionMode mode) { this.countrySelectionMode.set(mode); }
/*     */   public final ObjectProperty<SelectionMode> locationSelectionModeProperty() { return this.locationSelectionMode; }
/*     */   public final SelectionMode getLocationSelectionMode() { return (SelectionMode)this.locationSelectionMode.get(); }
/*     */   public final void setLocationSelectionMode(SelectionMode mode) { this.locationSelectionMode.set(mode); }
/*     */   public final DoubleProperty zoomFactorProperty() { return this.zoomFactor; }
/* 149 */   public WorldMapView() { this.countrySelectionMode = (ObjectProperty<SelectionMode>)new SimpleObjectProperty(this, "countrySelectionMode", SelectionMode.MULTIPLE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     this.locationSelectionMode = (ObjectProperty<SelectionMode>)new SimpleObjectProperty(this, "locationSelectionMode", SelectionMode.MULTIPLE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     this.zoomFactor = (DoubleProperty)new SimpleDoubleProperty(this, "zoomFactor", 1.0D)
/*     */       {
/*     */         public void set(double newValue) {
/* 216 */           super.set(Math.max(1.0D, Math.min(10.0D, newValue)));
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     this.selectedCountries = (ListProperty<Country>)new SimpleListProperty(this, "selectedCountries", FXCollections.observableArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     this.selectedLocations = (ListProperty<Location>)new SimpleListProperty(this, "selectedLocations", FXCollections.observableArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 307 */     this.countries = (ListProperty<Country>)new SimpleListProperty(this, "countries", FXCollections.observableArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     this.locations = (ListProperty<Location>)new SimpleListProperty(this, "locations", FXCollections.observableArrayList());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 364 */     this.showLocations = (BooleanProperty)new SimpleBooleanProperty(this, "showLocations", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 395 */     this.locationViewFactory = (ObjectProperty<Callback<Location, Node>>)new SimpleObjectProperty(this, "locationViewFactory");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 426 */     this.countryViewFactory = (ObjectProperty<Callback<Country, CountryView>>)new SimpleObjectProperty(this, "countryViewFactory"); getStyleClass().add("world-map"); setCountryViewFactory(country -> {
/*     */           CountryView view = new CountryView(country); view.setOnMouseEntered(()); Tooltip.install((Node)view, this.tooltip); return (Callback)view;
/*     */         }); setLocationViewFactory(location -> {
/*     */           Circle circle = new Circle(); circle.setRadius(4.0D); circle.setTranslateX(-4.0D); circle.setTranslateY(-4.0D); circle.setOnMouseEntered(()); Tooltip.install((Node)circle, this.tooltip);
/*     */           return (Callback)circle;
/*     */         }); }
/*     */   public final double getZoomFactor() { return this.zoomFactor.get(); }
/*     */   public final void setZoomFactor(double factor) { this.zoomFactor.set(factor); }
/* 434 */   public final ListProperty<Country> selectedCountriesProperty() { return this.selectedCountries; } public final ObjectProperty<Callback<Country, CountryView>> countryViewFactoryProperty() { return this.countryViewFactory; }
/*     */   public final ObservableList<Country> getSelectedCountries() { return (ObservableList<Country>)this.selectedCountries.get(); }
/*     */   public final void setSelectedCountries(ObservableList<Country> countries) { this.selectedCountries.set(countries); }
/*     */   public final ListProperty<Location> selectedLocationsProperty() { return this.selectedLocations; }
/*     */   public final ObservableList<Location> getSelectedLocations() { return (ObservableList<Location>)this.selectedLocations.get(); }
/*     */   public final void setSelectedLocations(ObservableList<Location> locations) { this.selectedLocations.set(locations); }
/*     */   public final ListProperty<Country> countriesProperty() { return this.countries; }
/*     */   public final ObservableList<Country> getCountries() { return (ObservableList<Country>)this.countries.get(); }
/*     */   public final void setCountries(ObservableList<Country> countries) { this.countries.set(countries); }
/* 443 */   public final ListProperty<Location> locationsProperty() { return this.locations; } public final ObservableList<Location> getLocations() { return (ObservableList<Location>)this.locations.get(); } public final Callback<Country, CountryView> getCountryViewFactory() { return (Callback<Country, CountryView>)this.countryViewFactory.get(); } public final void setLocations(ObservableList<Location> locations) { this.locations.set(locations); } public final BooleanProperty showLocationsProperty() { return this.showLocations; }
/*     */   public final boolean isShowLocations() { return this.showLocations.get(); }
/*     */   public final void setShowLocations(boolean show) { this.showLocations.set(show); }
/*     */   public final ObjectProperty<Callback<Location, Node>> locationViewFactoryProperty() { return this.locationViewFactory; }
/*     */   public final Callback<Location, Node> getLocationViewFactory() { return (Callback<Location, Node>)this.locationViewFactory.get(); }
/*     */   public final void setLocationViewFactory(Callback<Location, Node> factory) {
/*     */     this.locationViewFactory.set(factory);
/*     */   }
/*     */   public final void setCountryViewFactory(Callback<Country, CountryView> factory) {
/* 452 */     this.countryViewFactory.set(factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CountryView
/*     */     extends SVGPath
/*     */   {
/*     */     private final WorldMapView.Country country;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CountryView(WorldMapView.Country country) {
/* 469 */       this.country = Objects.<WorldMapView.Country>requireNonNull(country);
/*     */     }
/*     */     
/*     */     public final WorldMapView.Country getCountry() {
/* 473 */       return this.country;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 477 */       return this.country.name();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Country
/*     */   {
/* 488 */     AE((String)new String[0]),
/* 489 */     AO((String)new String[0]),
/* 490 */     AR((String)new String[0]),
/* 491 */     AT((String)new String[0]),
/* 492 */     AU((String)new String[0]),
/* 493 */     AZ((String)new String[0]),
/* 494 */     BA((String)new String[0]),
/* 495 */     BD((String)new String[0]),
/* 496 */     BE((String)new String[0]),
/* 497 */     BF((String)new String[0]),
/* 498 */     BG((String)new String[0]),
/* 499 */     BI((String)new String[0]),
/* 500 */     BJ((String)new String[0]),
/* 501 */     BN((String)new String[0]),
/* 502 */     BO((String)new String[0]),
/* 503 */     BR((String)new String[0]),
/* 504 */     BS((String)new String[0]),
/* 505 */     BT((String)new String[0]),
/* 506 */     BW((String)new String[0]),
/* 507 */     BY((String)new String[0]),
/* 508 */     BZ((String)new String[0]),
/* 509 */     CA((String)new String[0]),
/* 510 */     CD((String)new String[0]),
/* 511 */     CF((String)new String[0]),
/* 512 */     CG((String)new String[0]),
/* 513 */     CH((String)new String[0]),
/* 514 */     CI((String)new String[0]),
/* 515 */     CL((String)new String[0]),
/* 516 */     CM((String)new String[0]),
/* 517 */     CN((String)new String[0]),
/* 518 */     CO((String)new String[0]),
/* 519 */     CR((String)new String[0]),
/* 520 */     CU((String)new String[0]),
/* 521 */     CY((String)new String[0]),
/* 522 */     CZ((String)new String[0]),
/* 523 */     DE((String)new String[0]),
/* 524 */     DJ((String)new String[0]),
/* 525 */     DK((String)new String[0]),
/* 526 */     DO((String)new String[0]),
/* 527 */     DZ((String)new String[0]),
/* 528 */     EC((String)new String[0]),
/* 529 */     EE((String)new String[0]),
/* 530 */     EG((String)new String[0]),
/* 531 */     EH((String)new String[0]),
/* 532 */     ER((String)new String[0]),
/* 533 */     ES((String)new String[0]),
/* 534 */     ET((String)new String[0]),
/* 535 */     FK((String)new String[0]),
/* 536 */     FI((String)new String[0]),
/* 537 */     FJ((String)new String[0]),
/* 538 */     FR((String)new String[0]),
/* 539 */     GA((String)new String[0]),
/* 540 */     GB((String)new String[0]),
/* 541 */     GE((String)new String[0]),
/* 542 */     GF((String)new String[0]),
/* 543 */     GH((String)new String[0]),
/* 544 */     GL((String)new String[0]),
/* 545 */     GM((String)new String[0]),
/* 546 */     GN((String)new String[0]),
/* 547 */     GQ((String)new String[0]),
/* 548 */     GR((String)new String[0]),
/* 549 */     GT((String)new String[0]),
/* 550 */     GW((String)new String[0]),
/* 551 */     GY((String)new String[0]),
/* 552 */     HN((String)new String[0]),
/* 553 */     HR((String)new String[0]),
/* 554 */     HT((String)new String[0]),
/* 555 */     HU((String)new String[0]),
/* 556 */     ID((String)new String[0]),
/* 557 */     IE((String)new String[0]),
/* 558 */     IL((String)new String[0]),
/* 559 */     IN((String)new String[0]),
/* 560 */     IQ((String)new String[0]),
/* 561 */     IR((String)new String[0]),
/* 562 */     IS((String)new String[0]),
/* 563 */     IT((String)new String[0]),
/* 564 */     JM((String)new String[0]),
/* 565 */     JO((String)new String[0]),
/* 566 */     JP((String)new String[0]),
/* 567 */     KE((String)new String[0]),
/* 568 */     KG((String)new String[0]),
/* 569 */     KH((String)new String[0]),
/* 570 */     KP((String)new String[0]),
/* 571 */     KR((String)new String[0]),
/* 572 */     XK((String)new String[0]),
/* 573 */     KW((String)new String[0]),
/* 574 */     KZ((String)new String[0]),
/* 575 */     LA((String)new String[0]),
/* 576 */     LB((String)new String[0]),
/* 577 */     LK((String)new String[0]),
/* 578 */     LR((String)new String[0]),
/* 579 */     LS((String)new String[0]),
/* 580 */     LT((String)new String[0]),
/* 581 */     LU((String)new String[0]),
/* 582 */     LV((String)new String[0]),
/* 583 */     LY((String)new String[0]),
/* 584 */     MA((String)new String[0]),
/* 585 */     MD((String)new String[0]),
/* 586 */     ME((String)new String[0]),
/* 587 */     MG((String)new String[0]),
/* 588 */     MK((String)new String[0]),
/* 589 */     ML((String)new String[0]),
/* 590 */     MM((String)new String[0]),
/* 591 */     MN((String)new String[0]),
/* 592 */     MR((String)new String[0]),
/* 593 */     MW((String)new String[0]),
/* 594 */     MX((String)new String[0]),
/* 595 */     MY((String)new String[0]),
/* 596 */     MZ((String)new String[0]),
/* 597 */     NA((String)new String[0]),
/* 598 */     NC((String)new String[0]),
/* 599 */     NE((String)new String[0]),
/* 600 */     NG((String)new String[0]),
/* 601 */     NI((String)new String[0]),
/* 602 */     NL((String)new String[0]),
/* 603 */     NO((String)new String[0]),
/* 604 */     NP((String)new String[0]),
/* 605 */     NZ((String)new String[0]),
/* 606 */     OM((String)new String[0]),
/* 607 */     PA((String)new String[0]),
/* 608 */     PE((String)new String[0]),
/* 609 */     PG((String)new String[0]),
/* 610 */     PH((String)new String[0]),
/* 611 */     PL((String)new String[0]),
/* 612 */     PK((String)new String[0]),
/* 613 */     PR((String)new String[0]),
/* 614 */     PS((String)new String[0]),
/* 615 */     PT((String)new String[0]),
/* 616 */     PY((String)new String[0]),
/* 617 */     QA((String)new String[0]),
/* 618 */     RO((String)new String[0]),
/* 619 */     RS((String)new String[0]),
/* 620 */     RU((String)new String[0]),
/* 621 */     RW((String)new String[0]),
/* 622 */     SA((String)new String[0]),
/* 623 */     SB((String)new String[0]),
/* 624 */     SD((String)new String[0]),
/* 625 */     SE((String)new String[0]),
/* 626 */     SI((String)new String[0]),
/* 627 */     SJ((String)new String[0]),
/* 628 */     SK((String)new String[0]),
/* 629 */     SL((String)new String[0]),
/* 630 */     SN((String)new String[0]),
/* 631 */     SO((String)new String[0]),
/* 632 */     SR((String)new String[0]),
/* 633 */     SS((String)new String[0]),
/* 634 */     SV((String)new String[0]),
/* 635 */     SY((String)new String[0]),
/* 636 */     SZ((String)new String[0]),
/* 637 */     TD((String)new String[0]),
/* 638 */     TF((String)new String[0]),
/* 639 */     TG((String)new String[0]),
/* 640 */     TH((String)new String[0]),
/* 641 */     TJ((String)new String[0]),
/* 642 */     TL((String)new String[0]),
/* 643 */     TM((String)new String[0]),
/* 644 */     TN((String)new String[0]),
/* 645 */     TR((String)new String[0]),
/* 646 */     TT((String)new String[0]),
/* 647 */     TW((String)new String[0]),
/* 648 */     TZ((String)new String[0]),
/* 649 */     UA((String)new String[0]),
/* 650 */     UG((String)new String[0]),
/* 651 */     US((String)new String[0]),
/* 652 */     UY((String)new String[0]),
/* 653 */     UZ((String)new String[0]),
/* 654 */     VE((String)new String[0]),
/* 655 */     VN((String)new String[0]),
/* 656 */     VU((String)new String[0]),
/* 657 */     YE((String)new String[0]),
/* 658 */     ZA((String)new String[0]),
/* 659 */     ZM((String)new String[0]),
/* 660 */     ZW((String)new String[0]);
/*     */     
/*     */     private final Locale locale;
/*     */     
/*     */     Country(String... p) {
/* 665 */       this.locale = new Locale("", name());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Locale getLocale() {
/* 675 */       return this.locale;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Location
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private double latitude;
/*     */ 
/*     */ 
/*     */     
/*     */     private double longitude;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Location(double latitude, double longitude) {
/* 698 */       this("", latitude, longitude);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Location(String name, double latitude, double longitude) {
/* 709 */       this.name = name;
/* 710 */       this.latitude = latitude;
/* 711 */       this.longitude = longitude;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final String getName() {
/* 720 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final double getLatitude() {
/* 729 */       return this.latitude;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final double getLongitude() {
/* 738 */       return this.longitude;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\WorldMapView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
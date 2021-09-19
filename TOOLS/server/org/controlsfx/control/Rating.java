/*     */ package org.controlsfx.control;
/*     */ 
/*     */ import impl.org.controlsfx.skin.RatingSkin;
/*     */ import javafx.beans.property.BooleanProperty;
/*     */ import javafx.beans.property.DoubleProperty;
/*     */ import javafx.beans.property.IntegerProperty;
/*     */ import javafx.beans.property.ObjectProperty;
/*     */ import javafx.beans.property.SimpleBooleanProperty;
/*     */ import javafx.beans.property.SimpleDoubleProperty;
/*     */ import javafx.beans.property.SimpleIntegerProperty;
/*     */ import javafx.beans.property.SimpleObjectProperty;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.scene.control.Skin;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rating
/*     */   extends ControlsFXControl
/*     */ {
/*     */   private DoubleProperty rating;
/*     */   private IntegerProperty max;
/*     */   private ObjectProperty<Orientation> orientation;
/*     */   private BooleanProperty partialRating;
/*     */   private BooleanProperty updateOnHover;
/*     */   
/*     */   public Rating() {
/* 131 */     this(5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rating(int max) {
/* 141 */     this(max, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Skin<?> createDefaultSkin() {
/*     */     return (Skin<?>)new RatingSkin(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserAgentStylesheet() {
/*     */     return getUserAgentStylesheet(Rating.class, "rating.css");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final DoubleProperty ratingProperty() {
/*     */     return this.rating;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRating(double value) {
/*     */     ratingProperty().set(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getRating() {
/*     */     return (this.rating == null) ? 3.0D : this.rating.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final IntegerProperty maxProperty() {
/*     */     return this.max;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rating(int max, int rating) {
/* 189 */     this.rating = (DoubleProperty)new SimpleDoubleProperty(this, "rating", 3.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     this.max = (IntegerProperty)new SimpleIntegerProperty(this, "max", 5);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     this.partialRating = (BooleanProperty)new SimpleBooleanProperty(this, "partialRating", false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     this.updateOnHover = (BooleanProperty)new SimpleBooleanProperty(this, "updateOnHover", false);
/*     */     getStyleClass().setAll((Object[])new String[] { "rating" });
/*     */     setMax(max);
/*     */     setRating((rating == -1) ? (int)Math.floor(max / 2.0D) : rating);
/*     */   }
/*     */   
/*     */   public final void setUpdateOnHover(boolean value) {
/* 307 */     updateOnHoverProperty().set(value);
/*     */   } public final void setMax(int value) {
/*     */     maxProperty().set(value);
/*     */   }
/*     */   public final int getMax() {
/*     */     return (this.max == null) ? 5 : this.max.get();
/*     */   }
/*     */   public final boolean isUpdateOnHover() {
/* 315 */     return (this.updateOnHover == null) ? false : this.updateOnHover.get();
/*     */   }
/*     */   
/*     */   public final ObjectProperty<Orientation> orientationProperty() {
/*     */     if (this.orientation == null)
/*     */       this.orientation = (ObjectProperty<Orientation>)new SimpleObjectProperty(this, "orientation", Orientation.HORIZONTAL); 
/*     */     return this.orientation;
/*     */   }
/*     */   
/*     */   public final void setOrientation(Orientation value) {
/*     */     orientationProperty().set(value);
/*     */   }
/*     */   
/*     */   public final Orientation getOrientation() {
/*     */     return (this.orientation == null) ? Orientation.HORIZONTAL : (Orientation)this.orientation.get();
/*     */   }
/*     */   
/*     */   public final BooleanProperty partialRatingProperty() {
/*     */     return this.partialRating;
/*     */   }
/*     */   
/*     */   public final void setPartialRating(boolean value) {
/*     */     partialRatingProperty().set(value);
/*     */   }
/*     */   
/*     */   public final boolean isPartialRating() {
/*     */     return (this.partialRating == null) ? false : this.partialRating.get();
/*     */   }
/*     */   
/*     */   public final BooleanProperty updateOnHoverProperty() {
/*     */     return this.updateOnHover;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\Rating.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
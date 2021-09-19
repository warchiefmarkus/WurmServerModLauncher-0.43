/*     */ package org.controlsfx.tools;
/*     */ 
/*     */ import com.sun.javafx.webkit.Accessor;
/*     */ import com.sun.webkit.WebPage;
/*     */ import java.net.URL;
/*     */ import javafx.beans.value.ChangeListener;
/*     */ import javafx.beans.value.ObservableValue;
/*     */ import javafx.concurrent.Worker;
/*     */ import javafx.geometry.Rectangle2D;
/*     */ import javafx.scene.Parent;
/*     */ import javafx.scene.Scene;
/*     */ import javafx.scene.SnapshotParameters;
/*     */ import javafx.scene.SnapshotResult;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.image.WritableImage;
/*     */ import javafx.scene.paint.Color;
/*     */ import javafx.scene.paint.Paint;
/*     */ import javafx.scene.web.WebEngine;
/*     */ import javafx.scene.web.WebView;
/*     */ import javafx.stage.Stage;
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
/*     */ class SVGLoader
/*     */ {
/*     */   public static void loadSVGImage(URL svgImage, double prefWidth, double prefHeight, Callback<ImageView, Void> callback) {
/*  80 */     loadSVGImage(svgImage, prefWidth, prefHeight, callback, null);
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
/*     */   public static void loadSVGImage(URL svgImage, WritableImage outputImage) {
/*  95 */     if (outputImage == null) {
/*  96 */       throw new NullPointerException("outputImage can not be null");
/*     */     }
/*  98 */     double w = outputImage.getWidth();
/*  99 */     double h = outputImage.getHeight();
/* 100 */     loadSVGImage(svgImage, w, h, null, outputImage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void loadSVGImage(URL svgImage, final double prefWidth, final double prefHeight, final Callback<ImageView, Void> callback, final WritableImage outputImage) {
/* 108 */     final WebView view = new WebView();
/* 109 */     final WebEngine eng = view.getEngine();
/*     */ 
/*     */     
/* 112 */     WebPage webPage = Accessor.getPageFor(eng);
/* 113 */     webPage.setBackgroundColor(webPage.getMainFrame(), -256);
/* 114 */     webPage.setOpaque(webPage.getMainFrame(), false);
/*     */ 
/*     */ 
/*     */     
/* 118 */     Scene scene = new Scene((Parent)view);
/* 119 */     final Stage stage = new Stage();
/* 120 */     stage.setScene(scene);
/* 121 */     stage.setWidth(0.0D);
/* 122 */     stage.setHeight(0.0D);
/* 123 */     stage.setOpacity(0.0D);
/* 124 */     stage.show();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     String content = "<html><body style=\"margin-top: 0px; margin-bottom: 30px; margin-left: 0px; margin-right: 0px; padding: 0;\"><img id=\"svgImage\" style=\"display: block;float: top;\" width=\"" + prefWidth + "\" height=\"" + prefHeight + "\" src=\"" + svgImage.toExternalForm() + "\" /></body></head>";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     eng.loadContent(content);
/*     */     
/* 142 */     eng.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
/*     */           public void changed(ObservableValue<? extends Worker.State> o, Worker.State oldValue, Worker.State newValue) {
/* 144 */             if (newValue == Worker.State.SUCCEEDED) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 149 */               double svgWidth = (prefWidth >= 0.0D) ? prefWidth : SVGLoader.getSvgWidth(eng);
/* 150 */               double svgHeight = (prefHeight >= 0.0D) ? prefWidth : SVGLoader.getSvgHeight(eng);
/*     */               
/* 152 */               SnapshotParameters params = new SnapshotParameters();
/* 153 */               params.setFill((Paint)Color.TRANSPARENT);
/* 154 */               params.setViewport(new Rectangle2D(0.0D, 0.0D, svgWidth, svgHeight));
/*     */               
/* 156 */               view.snapshot(new Callback<SnapshotResult, Void>() {
/*     */                     public Void call(SnapshotResult param) {
/* 158 */                       WritableImage snapshot = param.getImage();
/* 159 */                       ImageView image = new ImageView((Image)snapshot);
/*     */                       
/* 161 */                       if (callback != null) {
/* 162 */                         callback.call(image);
/*     */                       }
/*     */                       
/* 165 */                       stage.hide();
/* 166 */                       return null;
/*     */                     }
/*     */                   },  params, outputImage);
/*     */             } 
/*     */           }
/*     */         });
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static double getSvgWidth(WebEngine webEngine) {
/* 192 */     Object result = getSvgDomProperty(webEngine, "offsetWidth");
/* 193 */     if (result instanceof Integer) {
/* 194 */       return ((Integer)result).intValue();
/*     */     }
/* 196 */     return -1.0D;
/*     */   }
/*     */   
/*     */   private static double getSvgHeight(WebEngine webEngine) {
/* 200 */     Object result = getSvgDomProperty(webEngine, "offsetHeight");
/* 201 */     if (result instanceof Integer) {
/* 202 */       return ((Integer)result).intValue();
/*     */     }
/* 204 */     return -1.0D;
/*     */   }
/*     */   
/*     */   private static Object getSvgDomProperty(WebEngine webEngine, String property) {
/* 208 */     return webEngine.executeScript("document.getElementById('svgImage')." + property);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\tools\SVGLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
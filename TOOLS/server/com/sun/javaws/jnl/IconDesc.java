/*    */ package com.sun.javaws.jnl;
/*    */ 
/*    */ import com.sun.deploy.xml.XMLAttributeBuilder;
/*    */ import com.sun.deploy.xml.XMLNode;
/*    */ import com.sun.deploy.xml.XMLable;
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IconDesc
/*    */   implements XMLable
/*    */ {
/*    */   private URL _location;
/*    */   private String _version;
/*    */   private String _locationString;
/*    */   private int _height;
/*    */   private int _width;
/*    */   private int _depth;
/*    */   private int _kind;
/*    */   public static final int ICON_KIND_DEFAULT = 0;
/*    */   public static final int ICON_KIND_SELECTED = 1;
/*    */   public static final int ICON_KIND_DISABLED = 2;
/*    */   public static final int ICON_KIND_ROLLOVER = 3;
/*    */   public static final int ICON_KIND_SPLASH = 4;
/*    */   
/*    */   public IconDesc(URL paramURL, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 29 */     this._location = paramURL;
/* 30 */     if (this._location != null) {
/* 31 */       this._locationString = this._location.toExternalForm();
/*    */     }
/* 33 */     this._width = paramInt2;
/* 34 */     this._depth = paramInt3;
/* 35 */     this._kind = paramInt4;
/* 36 */     this._version = paramString;
/*    */   }
/*    */   
/* 39 */   public URL getLocation() { return this._location; }
/* 40 */   public String getVersion() { return this._version; }
/* 41 */   public int getHeight() { return this._height; }
/* 42 */   public int getWidth() { return this._width; }
/* 43 */   public int getDepth() { return this._depth; } public int getKind() {
/* 44 */     return this._kind;
/*    */   }
/*    */   public final boolean equals(Object paramObject) {
/* 47 */     if (paramObject == this) {
/* 48 */       return true;
/*    */     }
/* 50 */     if (paramObject instanceof IconDesc) {
/* 51 */       IconDesc iconDesc = (IconDesc)paramObject;
/*    */       
/* 53 */       if (((this._version == null && iconDesc._version == null) || (this._version != null && this._version.equals(iconDesc._version))) && ((this._locationString == null && iconDesc._locationString == null) || (this._locationString != null && this._locationString.equals(iconDesc._locationString))))
/*    */       {
/*    */ 
/*    */ 
/*    */         
/* 58 */         return true;
/*    */       }
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 65 */     int i = 0;
/*    */     
/* 67 */     if (this._locationString != null) {
/* 68 */       i |= this._locationString.hashCode();
/*    */     }
/* 70 */     if (this._version != null) {
/* 71 */       i |= this._version.hashCode();
/*    */     }
/* 73 */     return i;
/*    */   }
/*    */   
/* 76 */   private static final String[] kinds = new String[] { "default", "selected", "disabled", "rollover", "splash" };
/*    */   
/*    */   public XMLNode asXML() {
/* 79 */     XMLAttributeBuilder xMLAttributeBuilder = new XMLAttributeBuilder();
/* 80 */     xMLAttributeBuilder.add("href", this._location);
/* 81 */     xMLAttributeBuilder.add("height", this._height);
/* 82 */     xMLAttributeBuilder.add("width", this._width);
/* 83 */     xMLAttributeBuilder.add("depth", this._depth);
/* 84 */     xMLAttributeBuilder.add("kind", kinds[this._kind]);
/* 85 */     return new XMLNode("icon", xMLAttributeBuilder.getAttributeList());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\jnl\IconDesc.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
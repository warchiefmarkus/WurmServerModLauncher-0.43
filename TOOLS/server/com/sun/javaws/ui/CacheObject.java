/*     */ package com.sun.javaws.ui;
/*     */ 
/*     */ import com.sun.deploy.resources.ResourceManager;
/*     */ import com.sun.deploy.util.Trace;
/*     */ import com.sun.javaws.LaunchDownload;
/*     */ import com.sun.javaws.LocalApplicationProperties;
/*     */ import com.sun.javaws.cache.Cache;
/*     */ import com.sun.javaws.cache.CacheImageLoader;
/*     */ import com.sun.javaws.cache.CacheImageLoaderCallback;
/*     */ import com.sun.javaws.cache.DiskCacheEntry;
/*     */ import com.sun.javaws.cache.DownloadProtocol;
/*     */ import com.sun.javaws.jnl.IconDesc;
/*     */ import com.sun.javaws.jnl.LaunchDesc;
/*     */ import com.sun.javaws.jnl.LaunchDescFactory;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Stroke;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.table.AbstractTableModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CacheObject
/*     */ {
/*  42 */   private static final DateFormat _df = DateFormat.getDateInstance();
/*     */   
/*  44 */   private static final String[] COLUMN_KEYS = new String[] { "jnlp.viewer.app.column", "jnlp.viewer.vendor.column", "jnlp.viewer.type.column", "jnlp.viewer.date.column", "jnlp.viewer.size.column", "jnlp.viewer.status.column" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private static final int _columns = COLUMN_KEYS.length;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _title;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _vendor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _type;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _date;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _size;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TLabel _status;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImageIcon _onlineIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImageIcon _offlineIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImageIcon _noLaunchIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImageIcon _java32;
/*     */ 
/*     */ 
/*     */   
/*     */   private final DiskCacheEntry _dce;
/*     */ 
/*     */ 
/*     */   
/*     */   private final AbstractTableModel _model;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int ICON_W = 32;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int ICON_H = 32;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _titleString;
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageIcon _icon;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _vendorString;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _typeString;
/*     */ 
/*     */ 
/*     */   
/*     */   private Date _theDate;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _dateString;
/*     */ 
/*     */ 
/*     */   
/*     */   private long _theSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _sizeString;
/*     */ 
/*     */ 
/*     */   
/*     */   private int _statusInt;
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageIcon _statusIcon;
/*     */ 
/*     */ 
/*     */   
/*     */   private String _statusText;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheObject(DiskCacheEntry paramDiskCacheEntry, AbstractTableModel paramAbstractTableModel) {
/* 169 */     this.ICON_W = 32;
/* 170 */     this.ICON_H = 32;
/* 171 */     this._titleString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     this._icon = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 197 */     this._vendorString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 209 */     this._typeString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 234 */     this._theDate = null;
/* 235 */     this._dateString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     this._theSize = 0L;
/* 255 */     this._sizeString = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 274 */     this._statusInt = -1;
/* 275 */     this._statusIcon = null;
/* 276 */     this._statusText = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 409 */     this._ld = null;
/* 410 */     this._lap = null; this._dce = paramDiskCacheEntry; this._model = paramAbstractTableModel; if (_title == null) { _title = new TLabel(this, 2); _vendor = new TLabel(this, 2); _type = new TLabel(this, 0); _date = new TLabel(this, 4); _size = new TLabel(this, 4); _status = new TLabel(this, 0); _java32 = new ViewerIcon(this, 0, 0, ResourceManager.class.getResource("image/java32.png")); _onlineIcon = new ViewerIcon(this, 0, 0, ResourceManager.class.getResource("image/online.gif")); _offlineIcon = new ViewerIcon(this, 0, 0, ResourceManager.class.getResource("image/offline.gif")); _noLaunchIcon = null; } 
/*     */   }
/*     */   public static String getColumnName(int paramInt) { return ResourceManager.getMessage(COLUMN_KEYS[paramInt]); }
/* 413 */   public static int getColumnCount() { return _columns; } public static String getHeaderToolTipText(int paramInt) { return ResourceManager.getString(COLUMN_KEYS[paramInt] + ".tooltip"); } public static int getPreferredWidth(int paramInt) { if (paramInt < _columns) switch (paramInt) { case 0: return 192;case 1: return 140;case 2: return 70;case 3: return 70;case 4: return 64;case 5: return 64; }   throw new ArrayIndexOutOfBoundsException("column index: " + paramInt); } public static Class getClass(int paramInt) { if (paramInt < _columns) switch (paramInt) { case 0: return JLabel.class;case 1: return JLabel.class;case 2: return JLabel.class;case 3: return JLabel.class;case 4: return JLabel.class;case 5: return JLabel.class; }   throw new ArrayIndexOutOfBoundsException("column index: " + paramInt); } public Object getObject(int paramInt) { if (paramInt < _columns) switch (paramInt) { case 0: return getTitleLabel();case 1: return getVendorLabel();case 2: return getTypeLabel();case 3: return getDateLabel();case 4: return getSizeLabel();case 5: return getStatusLabel(); }   throw new ArrayIndexOutOfBoundsException("column index: " + paramInt); } public boolean isEditable(int paramInt) { return false; } public void setValue(int paramInt, Object paramObject) {} public String getTitleString() { if (this._titleString == null) this._titleString = getTitle();  return this._titleString; } private JLabel getTitleLabel() { if (this._icon == null) { File file = getIconFile(); if (file != null) this._icon = new ViewerIcon(this, 32, 32, file.getPath());  if (this._icon == null) this._icon = _java32;  }  if (this._icon != null && this._icon.getIconWidth() > 0 && this._icon.getIconHeight() > 0) _title.setIcon(this._icon);  _title.setText(getTitleString()); return _title; } public String getVendorString() { if (this._vendorString == null) this._vendorString = getVendor();  return this._vendorString; } private TLabel getVendorLabel() { _vendor.setText(getVendorString()); return _vendor; } public String getTypeString() { if (this._typeString == null) this._typeString = getLaunchTypeString(getLaunchDesc().getLaunchType());  return this._typeString; } public static String getLaunchTypeString(int paramInt) { switch (paramInt) { case 1: return ResourceManager.getMessage("jnlp.viewer.application");case 2: return ResourceManager.getMessage("jnlp.viewer.applet");case 3: return ResourceManager.getMessage("jnlp.viewer.extension");case 4: return ResourceManager.getMessage("jnlp.viewer.installer"); }  return ""; } private TLabel getTypeLabel() { _type.setText(getTypeString()); return _type; } public Date getDate() { if (this._dateString == null) { this._theDate = getLastAccesed(); if (this._theDate != null) { this._dateString = _df.format(this._theDate); } else { this._dateString = ""; }  }  return this._theDate; } public LaunchDesc getLaunchDesc() { if (this._ld == null) {
/*     */       try {
/* 415 */         this._ld = LaunchDescFactory.buildDescriptor(this._dce.getFile());
/* 416 */       } catch (Exception exception) {
/* 417 */         Trace.ignoredException(exception);
/*     */       } 
/*     */     }
/* 420 */     return this._ld; }
/*     */   private TLabel getDateLabel() { getDate(); _date.setText(this._dateString); return _date; }
/*     */   public long getSize() { if (this._sizeString == null) { this._theSize = getResourceSize(); if (this._theSize > 10240L) { this._sizeString = " " + (this._theSize / 1024L) + " KB"; } else { this._sizeString = " " + (this._theSize / 1024L) + "." + (this._theSize % 1024L / 102L) + " KB"; }  }  return this._theSize; }
/*     */   private TLabel getSizeLabel() { getSize(); _size.setText(this._sizeString); return _size; } public int getStatus() { if (this._statusInt < 0) { if (canLaunchOffline()) { this._statusInt = 2; } else { this._statusInt = hasHref() ? 1 : 0; }  switch (this._statusInt) { case 0: this._statusIcon = _noLaunchIcon; if (getLaunchDesc().isApplicationDescriptor()) { this._statusText = ResourceManager.getString("jnlp.viewer.norun1.tooltip", getTypeString()); break; }  this._statusText = ResourceManager.getString("jnlp.viewer.norun2.tooltip"); break;case 1: this._statusIcon = _onlineIcon; this._statusText = ResourceManager.getString("jnlp.viewer.online.tooltip", getTypeString()); break;case 2: this._statusIcon = _offlineIcon; this._statusText = ResourceManager.getString("jnlp.viewer.offline.tooltip", getTypeString()); break; }  }  return this._statusInt; } private TLabel getStatusLabel() { getStatus(); if (this._statusIcon == null || (this._statusIcon.getIconWidth() > 0 && this._statusIcon.getIconHeight() > 0)) { _status.setIcon(this._statusIcon); _status.setToolTipText(this._statusText); }  return _status; } public static void hasFocus(Component paramComponent, boolean paramBoolean) { if (paramComponent instanceof TLabel) ((TLabel)paramComponent).hasFocus(paramBoolean);  } public int compareColumns(CacheObject paramCacheObject, int paramInt) { switch (paramInt) { case 0: return compareStrings(getTitleString(), paramCacheObject.getTitleString());case 1: return compareStrings(getVendorString(), paramCacheObject.getVendorString());case 2: return compareStrings(getTypeString(), paramCacheObject.getTypeString());case 3: return compareDates(getDate(), paramCacheObject.getDate());case 4: return compareLong(getSize(), paramCacheObject.getSize()); }  return compareInt(getStatus(), paramCacheObject.getStatus()); } private static final float[] dash = new float[] { 1.0F, 2.0F }; private static final BasicStroke _dashed = new BasicStroke(1.0F, 2, 0, 10.0F, dash, 0.0F); LaunchDesc _ld; LocalApplicationProperties _lap; private class TLabel extends JLabel {
/* 424 */     boolean _focus; private final CacheObject this$0; public TLabel(CacheObject this$0, int param1Int) { this.this$0 = this$0; this._focus = false; setOpaque(true); setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4)); setHorizontalAlignment(param1Int); } public void paint(Graphics param1Graphics) { super.paint(param1Graphics); if (this._focus && param1Graphics instanceof Graphics2D) { Stroke stroke = ((Graphics2D)param1Graphics).getStroke(); ((Graphics2D)param1Graphics).setStroke(CacheObject._dashed); param1Graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1); ((Graphics2D)param1Graphics).setStroke(stroke); }  } public void hasFocus(boolean param1Boolean) { this._focus = param1Boolean; } } private int compareStrings(String paramString1, String paramString2) { if (paramString1 == paramString2) return 0;  if (paramString1 == null) return -1;  if (paramString2 == null) return 1;  return paramString1.compareTo(paramString2); } private int compareDates(Date paramDate1, Date paramDate2) { if (paramDate1 == paramDate2) return 0;  if (paramDate1 == null) return -1;  if (paramDate2 == null) return 1;  return compareLong(paramDate1.getTime(), paramDate2.getTime()); } private int compareLong(long paramLong1, long paramLong2) { if (paramLong1 == paramLong2) return 0;  return (paramLong1 < paramLong2) ? -1 : 1; } private int compareInt(int paramInt1, int paramInt2) { if (paramInt1 == paramInt2) return 0;  return (paramInt1 < paramInt2) ? -1 : 1; } public DiskCacheEntry getDCE() { return this._dce; } public LocalApplicationProperties getLocalApplicationProperties() { if (this._lap == null) {
/* 425 */       this._lap = Cache.getLocalApplicationProperties(this._dce, getLaunchDesc());
/*     */     }
/* 427 */     return this._lap; }
/*     */ 
/*     */   
/*     */   public File getJnlpFile() {
/* 431 */     return this._dce.getFile();
/*     */   }
/*     */   
/*     */   public String getTitle() {
/*     */     try {
/* 436 */       return getLaunchDesc().getInformation().getTitle();
/* 437 */     } catch (Exception exception) {
/* 438 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getVendor() {
/*     */     try {
/* 444 */       return getLaunchDesc().getInformation().getVendor();
/* 445 */     } catch (Exception exception) {
/* 446 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getHref() {
/* 451 */     URL uRL = getLaunchDesc().getLocation();
/* 452 */     if (uRL != null) return uRL.toString(); 
/* 453 */     return null;
/*     */   }
/*     */   
/*     */   public File getIconFile() {
/*     */     try {
/* 458 */       IconDesc iconDesc = getLaunchDesc().getInformation().getIconLocation(1, 0);
/*     */       
/* 460 */       DiskCacheEntry diskCacheEntry = DownloadProtocol.getCachedVersion(iconDesc.getLocation(), iconDesc.getVersion(), 2);
/*     */ 
/*     */       
/* 463 */       if (diskCacheEntry != null) {
/* 464 */         return diskCacheEntry.getFile();
/*     */       }
/* 466 */     } catch (Exception exception) {}
/*     */     
/* 468 */     return null;
/*     */   }
/*     */   
/*     */   public Date getLastAccesed() {
/* 472 */     return getLocalApplicationProperties().getLastAccessed();
/*     */   }
/*     */   
/*     */   public long getResourceSize() {
/* 476 */     return LaunchDownload.getCachedSize(getLaunchDesc());
/*     */   }
/*     */   
/*     */   public boolean inFilter(int paramInt) {
/* 480 */     return (paramInt == 0 || paramInt == getLaunchDesc().getLaunchType());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasHref() {
/* 485 */     if (getLaunchDesc().isApplicationDescriptor()) {
/* 486 */       return (this._ld.getLocation() != null);
/*     */     }
/* 488 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canLaunchOffline() {
/* 492 */     if (getLaunchDesc().isApplicationDescriptor()) {
/* 493 */       return this._ld.getInformation().supportsOfflineOperation();
/*     */     }
/* 495 */     return false;
/*     */   }
/*     */   
/*     */   private class ViewerIcon extends ImageIcon implements CacheImageLoaderCallback {
/*     */     private int _width;
/*     */     private int _height;
/*     */     private final CacheObject this$0;
/*     */     
/*     */     public ViewerIcon(CacheObject this$0, int param1Int1, int param1Int2, String param1String) {
/* 504 */       this.this$0 = this$0;
/*     */       
/* 506 */       this._width = param1Int1;
/* 507 */       this._height = param1Int2;
/*     */       try {
/* 509 */         URL uRL = (new File(param1String)).toURL();
/* 510 */         if (uRL != null) {
/* 511 */           CacheImageLoader.getInstance().loadImage(uRL, this);
/*     */         }
/* 513 */       } catch (Exception exception) {
/* 514 */         Trace.ignoredException(exception);
/*     */       } 
/*     */     }
/*     */     public ViewerIcon(CacheObject this$0, int param1Int1, int param1Int2, URL param1URL) {
/* 518 */       this.this$0 = this$0;
/*     */       
/* 520 */       this._width = param1Int1;
/* 521 */       this._height = param1Int2;
/* 522 */       if (param1URL != null) {
/* 523 */         CacheImageLoader.getInstance().loadImage(param1URL, this);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void imageAvailable(IconDesc param1IconDesc, Image param1Image, File param1File) {
/* 532 */       int i = param1Image.getWidth(null);
/* 533 */       int j = param1Image.getHeight(null);
/* 534 */       Image image = param1Image;
/* 535 */       (new Thread(new Runnable(this, image, i, j) { private final Image val$imageIn; private final int val$w; private final int val$h; private final CacheObject.ViewerIcon this$1;
/*     */             public void run() {
/* 537 */               Image image = this.val$imageIn;
/* 538 */               if (this.this$1._width > 0 && this.this$1._height > 0 && (this.this$1._width != this.val$w || this.this$1._height != this.val$h))
/*     */               {
/* 540 */                 image = this.val$imageIn.getScaledInstance(this.this$1._width, this.this$1._height, 1);
/*     */               }
/*     */               
/* 543 */               this.this$1.setImage(image);
/* 544 */               this.this$1.this$0._model.fireTableDataChanged();
/*     */             } }
/*     */         )).start();
/*     */     }
/*     */     
/*     */     public void finalImageAvailable(IconDesc param1IconDesc, Image param1Image, File param1File) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaw\\ui\CacheObject.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */
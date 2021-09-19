/*     */ package org.seamless.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*     */ import javax.swing.plaf.metal.MetalTabbedPaneUI;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClosableTabbedPane
/*     */   extends JTabbedPane
/*     */   implements MouseListener, MouseMotionListener
/*     */ {
/*  40 */   private EventListenerList listenerList = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  45 */   private JViewport headerViewport = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private Icon normalCloseIcon = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private Icon hooverCloseIcon = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private Icon pressedCloseIcon = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClosableTabbedPane() {
/*  67 */     init(2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClosableTabbedPane(int horizontalTextPosition) {
/*  78 */     init(horizontalTextPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(int horizontalTextPosition) {
/*  88 */     this.listenerList = new EventListenerList();
/*  89 */     addMouseListener(this);
/*  90 */     addMouseMotionListener(this);
/*     */     
/*  92 */     if (getUI() instanceof MetalTabbedPaneUI) {
/*  93 */       setUI(new CloseableMetalTabbedPaneUI(horizontalTextPosition));
/*     */     } else {
/*  95 */       setUI(new CloseableTabbedPaneUI(horizontalTextPosition));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloseIcons(Icon normal, Icon hoover, Icon pressed) {
/* 106 */     this.normalCloseIcon = normal;
/* 107 */     this.hooverCloseIcon = hoover;
/* 108 */     this.pressedCloseIcon = pressed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTab(String title, Component component) {
/* 118 */     addTab(title, component, (Icon)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTab(String title, Component component, Icon extraIcon) {
/* 129 */     boolean doPaintCloseIcon = true;
/*     */     try {
/* 131 */       Object prop = null;
/* 132 */       if ((prop = ((JComponent)component).getClientProperty("isClosable")) != null)
/*     */       {
/* 134 */         doPaintCloseIcon = ((Boolean)prop).booleanValue();
/*     */       }
/* 136 */     } catch (Exception ignored) {}
/*     */     
/* 138 */     addTab(title, doPaintCloseIcon ? new CloseTabIcon(extraIcon) : null, component);
/*     */ 
/*     */ 
/*     */     
/* 142 */     if (this.headerViewport == null) {
/* 143 */       for (Component c : getComponents()) {
/* 144 */         if ("TabbedPane.scrollableViewport".equals(c.getName())) {
/* 145 */           this.headerViewport = (JViewport)c;
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseClicked(MouseEvent e) {
/* 157 */     processMouseEvents(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseEntered(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseExited(MouseEvent e) {
/* 174 */     for (int i = 0; i < getTabCount(); i++) {
/* 175 */       CloseTabIcon icon = (CloseTabIcon)getIconAt(i);
/* 176 */       if (icon != null)
/* 177 */         icon.mouseover = false; 
/*     */     } 
/* 179 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent e) {
/* 188 */     processMouseEvents(e);
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
/*     */   public void mouseReleased(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseDragged(MouseEvent e) {
/* 213 */     processMouseEvents(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {
/* 223 */     processMouseEvents(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processMouseEvents(MouseEvent e) {
/* 232 */     int tabNumber = getUI().tabForCoordinate(this, e.getX(), e.getY());
/* 233 */     if (tabNumber < 0)
/* 234 */       return;  CloseTabIcon icon = (CloseTabIcon)getIconAt(tabNumber);
/* 235 */     if (icon != null) {
/* 236 */       Rectangle rect = icon.getBounds();
/* 237 */       Point pos = (this.headerViewport == null) ? new Point() : this.headerViewport.getViewPosition();
/*     */       
/* 239 */       Rectangle drawRect = new Rectangle(rect.x - pos.x, rect.y - pos.y, rect.width, rect.height);
/*     */ 
/*     */       
/* 242 */       if (e.getID() == 501) {
/* 243 */         icon.mousepressed = (e.getModifiers() == 16);
/* 244 */         repaint(drawRect);
/* 245 */       } else if (e.getID() == 503 || e.getID() == 506 || e.getID() == 500) {
/*     */         
/* 247 */         pos.x += e.getX();
/* 248 */         pos.y += e.getY();
/* 249 */         if (rect.contains(pos)) {
/* 250 */           if (e.getID() == 500) {
/* 251 */             int selIndex = getSelectedIndex();
/* 252 */             if (fireCloseTab(selIndex)) {
/* 253 */               if (selIndex > 0) {
/*     */                 
/* 255 */                 Rectangle rec = getUI().getTabBounds(this, selIndex - 1);
/*     */                 
/* 257 */                 MouseEvent event = new MouseEvent((Component)e.getSource(), e.getID() + 1, System.currentTimeMillis(), e.getModifiers(), rec.x, rec.y, e.getClickCount(), e.isPopupTrigger(), e.getButton());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 266 */                 dispatchEvent(event);
/*     */               } 
/*     */ 
/*     */               
/* 270 */               remove(selIndex);
/*     */             } else {
/* 272 */               icon.mouseover = false;
/* 273 */               icon.mousepressed = false;
/* 274 */               repaint(drawRect);
/*     */             } 
/*     */           } else {
/* 277 */             icon.mouseover = true;
/* 278 */             icon.mousepressed = (e.getModifiers() == 16);
/*     */           } 
/*     */         } else {
/* 281 */           icon.mouseover = false;
/*     */         } 
/* 283 */         repaint(drawRect);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCloseableTabbedPaneListener(ClosableTabbedPaneListener l) {
/* 294 */     this.listenerList.add(ClosableTabbedPaneListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeCloseableTabbedPaneListener(ClosableTabbedPaneListener l) {
/* 303 */     this.listenerList.remove(ClosableTabbedPaneListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClosableTabbedPaneListener[] getCloseableTabbedPaneListener() {
/* 314 */     return this.listenerList.<ClosableTabbedPaneListener>getListeners(ClosableTabbedPaneListener.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean fireCloseTab(int tabIndexToClose) {
/* 325 */     boolean closeit = true;
/*     */     
/* 327 */     Object[] listeners = this.listenerList.getListenerList();
/* 328 */     for (Object i : listeners) {
/* 329 */       if (i instanceof ClosableTabbedPaneListener && 
/* 330 */         !((ClosableTabbedPaneListener)i).closeTab(tabIndexToClose)) {
/* 331 */         closeit = false;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 336 */     return closeit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class CloseTabIcon
/*     */     implements Icon
/*     */   {
/*     */     private int x_pos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int y_pos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int width;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int height;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Icon fileIcon;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean mouseover = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean mousepressed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseTabIcon(Icon fileIcon) {
/* 386 */       this.fileIcon = fileIcon;
/* 387 */       this.width = 16;
/* 388 */       this.height = 16;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 402 */       boolean doPaintCloseIcon = true;
/*     */       
/*     */       try {
/* 405 */         JTabbedPane tabbedpane = (JTabbedPane)c;
/* 406 */         int tabNumber = tabbedpane.getUI().tabForCoordinate(tabbedpane, x, y);
/* 407 */         JComponent curPanel = (JComponent)tabbedpane.getComponentAt(tabNumber);
/* 408 */         Object prop = null;
/* 409 */         if ((prop = curPanel.getClientProperty("isClosable")) != null) {
/* 410 */           doPaintCloseIcon = ((Boolean)prop).booleanValue();
/*     */         }
/* 412 */       } catch (Exception ignored) {}
/* 413 */       if (doPaintCloseIcon) {
/* 414 */         this.x_pos = x;
/* 415 */         this.y_pos = y;
/* 416 */         int y_p = y + 1;
/*     */         
/* 418 */         if (ClosableTabbedPane.this.normalCloseIcon != null && !this.mouseover) {
/* 419 */           ClosableTabbedPane.this.normalCloseIcon.paintIcon(c, g, x, y_p);
/* 420 */         } else if (ClosableTabbedPane.this.hooverCloseIcon != null && this.mouseover && !this.mousepressed) {
/* 421 */           ClosableTabbedPane.this.hooverCloseIcon.paintIcon(c, g, x, y_p);
/* 422 */         } else if (ClosableTabbedPane.this.pressedCloseIcon != null && this.mousepressed) {
/* 423 */           ClosableTabbedPane.this.pressedCloseIcon.paintIcon(c, g, x, y_p);
/*     */         } else {
/* 425 */           y_p++;
/*     */           
/* 427 */           Color col = g.getColor();
/*     */           
/* 429 */           if (this.mousepressed && this.mouseover) {
/* 430 */             g.setColor(Color.WHITE);
/* 431 */             g.fillRect(x + 1, y_p, 12, 13);
/*     */           } 
/*     */           
/* 434 */           g.setColor(Color.black);
/* 435 */           g.drawLine(x + 1, y_p, x + 12, y_p);
/* 436 */           g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
/* 437 */           g.drawLine(x, y_p + 1, x, y_p + 12);
/* 438 */           g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
/* 439 */           g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
/* 440 */           if (this.mouseover)
/* 441 */             g.setColor(Color.GRAY); 
/* 442 */           g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
/* 443 */           g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
/* 444 */           g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
/* 445 */           g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
/* 446 */           g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
/* 447 */           g.setColor(col);
/* 448 */           if (this.fileIcon != null) {
/* 449 */             this.fileIcon.paintIcon(c, g, x + this.width, y_p);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getIconWidth() {
/* 461 */       return this.width + ((this.fileIcon != null) ? this.fileIcon.getIconWidth() : 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getIconHeight() {
/* 470 */       return this.height;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Rectangle getBounds() {
/* 481 */       return new Rectangle(this.x_pos, this.y_pos, this.width, this.height);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class CloseableTabbedPaneUI
/*     */     extends BasicTabbedPaneUI
/*     */   {
/* 493 */     private int horizontalTextPosition = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseableTabbedPaneUI() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseableTabbedPaneUI(int horizontalTextPosition) {
/* 508 */       this.horizontalTextPosition = horizontalTextPosition;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
/* 529 */       textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
/*     */       
/* 531 */       View v = getTextViewForTab(tabIndex);
/* 532 */       if (v != null) {
/* 533 */         this.tabPane.putClientProperty("html", v);
/*     */       }
/*     */       
/* 536 */       SwingUtilities.layoutCompoundLabel(this.tabPane, metrics, title, icon, 0, 0, 0, this.horizontalTextPosition, tabRect, iconRect, textRect, this.textIconGap + 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 548 */       this.tabPane.putClientProperty("html", (Object)null);
/*     */       
/* 550 */       int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
/* 551 */       int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
/* 552 */       iconRect.x += xNudge;
/* 553 */       iconRect.y += yNudge;
/* 554 */       textRect.x += xNudge;
/* 555 */       textRect.y += yNudge;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class CloseableMetalTabbedPaneUI
/*     */     extends MetalTabbedPaneUI
/*     */   {
/* 567 */     private int horizontalTextPosition = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseableMetalTabbedPaneUI() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CloseableMetalTabbedPaneUI(int horizontalTextPosition) {
/* 582 */       this.horizontalTextPosition = horizontalTextPosition;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
/* 603 */       textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
/*     */       
/* 605 */       View v = getTextViewForTab(tabIndex);
/* 606 */       if (v != null) {
/* 607 */         this.tabPane.putClientProperty("html", v);
/*     */       }
/*     */       
/* 610 */       SwingUtilities.layoutCompoundLabel(this.tabPane, metrics, title, icon, 0, 0, 0, this.horizontalTextPosition, tabRect, iconRect, textRect, this.textIconGap + 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 622 */       this.tabPane.putClientProperty("html", (Object)null);
/*     */       
/* 624 */       int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
/* 625 */       int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
/* 626 */       iconRect.x += xNudge;
/* 627 */       iconRect.y += yNudge;
/* 628 */       textRect.x += xNudge;
/* 629 */       textRect.y += yNudge;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\seamless\swing\ClosableTabbedPane.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */
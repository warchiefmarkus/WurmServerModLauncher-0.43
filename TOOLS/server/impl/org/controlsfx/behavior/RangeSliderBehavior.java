/*     */ package impl.org.controlsfx.behavior;
/*     */ 
/*     */ import com.sun.javafx.scene.control.behavior.BehaviorBase;
/*     */ import com.sun.javafx.scene.control.behavior.KeyBinding;
/*     */ import com.sun.javafx.scene.control.behavior.OrientedKeyBinding;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javafx.event.EventType;
/*     */ import javafx.geometry.Orientation;
/*     */ import javafx.scene.control.Control;
/*     */ import javafx.scene.input.KeyCode;
/*     */ import javafx.scene.input.KeyEvent;
/*     */ import javafx.scene.input.MouseEvent;
/*     */ import javafx.util.Callback;
/*     */ import org.controlsfx.control.RangeSlider;
/*     */ import org.controlsfx.tools.Utils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RangeSliderBehavior
/*     */   extends BehaviorBase<RangeSlider>
/*     */ {
/*  69 */   private static final List<KeyBinding> RANGESLIDER_BINDINGS = new ArrayList<>();
/*     */   static {
/*  71 */     RANGESLIDER_BINDINGS.add((new KeyBinding(KeyCode.F4, "TraverseDebug")).alt().ctrl().shift());
/*     */     
/*  73 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.LEFT, "DecrementValue"));
/*  74 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.KP_LEFT, "DecrementValue"));
/*  75 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.UP, "IncrementValue")).vertical());
/*  76 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.KP_UP, "IncrementValue")).vertical());
/*  77 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.RIGHT, "IncrementValue"));
/*  78 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.KP_RIGHT, "IncrementValue"));
/*  79 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.DOWN, "DecrementValue")).vertical());
/*  80 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.KP_DOWN, "DecrementValue")).vertical());
/*     */     
/*  82 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.LEFT, "TraverseLeft")).vertical());
/*  83 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.KP_LEFT, "TraverseLeft")).vertical());
/*  84 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.UP, "TraverseUp"));
/*  85 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.KP_UP, "TraverseUp"));
/*  86 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.RIGHT, "TraverseRight")).vertical());
/*  87 */     RANGESLIDER_BINDINGS.add((new RangeSliderKeyBinding(KeyCode.KP_RIGHT, "TraverseRight")).vertical());
/*  88 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.DOWN, "TraverseDown"));
/*  89 */     RANGESLIDER_BINDINGS.add(new RangeSliderKeyBinding(KeyCode.KP_DOWN, "TraverseDown"));
/*     */     
/*  91 */     RANGESLIDER_BINDINGS.add(new KeyBinding(KeyCode.HOME, KeyEvent.KEY_RELEASED, "Home"));
/*  92 */     RANGESLIDER_BINDINGS.add(new KeyBinding(KeyCode.END, KeyEvent.KEY_RELEASED, "End"));
/*     */   }
/*     */   private Callback<Void, FocusedChild> selectedValue;
/*     */   public RangeSliderBehavior(RangeSlider slider) {
/*  96 */     super((Control)slider, RANGESLIDER_BINDINGS);
/*     */   }
/*     */   
/*     */   protected void callAction(String s) {
/* 100 */     if ("Home".equals(s) || "Home2".equals(s)) { home(); }
/* 101 */     else if ("End".equals(s) || "End2".equals(s)) { end(); }
/* 102 */     else if ("IncrementValue".equals(s) || "IncrementValue2".equals(s)) { incrementValue(); }
/* 103 */     else if ("DecrementValue".equals(s) || "DecrementValue2".equals(s)) { decrementValue(); }
/* 104 */     else { super.callAction(s); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSelectedValue(Callback<Void, FocusedChild> c) {
/* 113 */     this.selectedValue = c;
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
/*     */   public void trackPress(MouseEvent e, double position) {
/* 126 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/*     */     
/* 128 */     if (!rangeSlider.isFocused()) {
/* 129 */       rangeSlider.requestFocus();
/*     */     }
/* 131 */     if (this.selectedValue != null) {
/*     */       double newPosition;
/* 133 */       if (rangeSlider.getOrientation().equals(Orientation.HORIZONTAL)) {
/* 134 */         newPosition = position * (rangeSlider.getMax() - rangeSlider.getMin()) + rangeSlider.getMin();
/*     */       } else {
/* 136 */         newPosition = (1.0D - position) * (rangeSlider.getMax() - rangeSlider.getMin()) + rangeSlider.getMin();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       if (newPosition < rangeSlider.getLowValue()) {
/* 145 */         rangeSlider.adjustLowValue(newPosition);
/*     */       } else {
/* 147 */         rangeSlider.adjustHighValue(newPosition);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trackRelease(MouseEvent e, double position) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lowThumbPressed(MouseEvent e, double position) {
/* 163 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/* 164 */     if (!rangeSlider.isFocused()) rangeSlider.requestFocus(); 
/* 165 */     rangeSlider.setLowValueChanging(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lowThumbDragged(MouseEvent e, double position) {
/* 173 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/* 174 */     double newValue = Utils.clamp(rangeSlider.getMin(), position * (rangeSlider
/* 175 */         .getMax() - rangeSlider.getMin()) + rangeSlider.getMin(), rangeSlider
/* 176 */         .getMax());
/* 177 */     rangeSlider.setLowValue(newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lowThumbReleased(MouseEvent e) {
/* 184 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/* 185 */     rangeSlider.setLowValueChanging(false);
/*     */ 
/*     */     
/* 188 */     if (rangeSlider.isSnapToTicks()) {
/* 189 */       rangeSlider.setLowValue(snapValueToTicks(rangeSlider.getLowValue()));
/*     */     }
/*     */   }
/*     */   
/*     */   void home() {
/* 194 */     RangeSlider slider = (RangeSlider)getControl();
/* 195 */     slider.adjustHighValue(slider.getMin());
/*     */   }
/*     */   
/*     */   void decrementValue() {
/* 199 */     RangeSlider slider = (RangeSlider)getControl();
/* 200 */     if (this.selectedValue != null) {
/* 201 */       if (this.selectedValue.call(null) == FocusedChild.HIGH_THUMB) {
/* 202 */         if (slider.isSnapToTicks()) {
/* 203 */           slider.adjustHighValue(slider.getHighValue() - computeIncrement());
/*     */         } else {
/* 205 */           slider.decrementHighValue();
/*     */         } 
/* 207 */       } else if (slider.isSnapToTicks()) {
/* 208 */         slider.adjustLowValue(slider.getLowValue() - computeIncrement());
/*     */       } else {
/* 210 */         slider.decrementLowValue();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   void end() {
/* 216 */     RangeSlider slider = (RangeSlider)getControl();
/* 217 */     slider.adjustHighValue(slider.getMax());
/*     */   }
/*     */   
/*     */   void incrementValue() {
/* 221 */     RangeSlider slider = (RangeSlider)getControl();
/* 222 */     if (this.selectedValue != null) {
/* 223 */       if (this.selectedValue.call(null) == FocusedChild.HIGH_THUMB) {
/* 224 */         if (slider.isSnapToTicks()) {
/* 225 */           slider.adjustHighValue(slider.getHighValue() + computeIncrement());
/*     */         } else {
/* 227 */           slider.incrementHighValue();
/*     */         } 
/* 229 */       } else if (slider.isSnapToTicks()) {
/* 230 */         slider.adjustLowValue(slider.getLowValue() + computeIncrement());
/*     */       } else {
/* 232 */         slider.incrementLowValue();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   double computeIncrement() {
/* 239 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/* 240 */     double d = 0.0D;
/* 241 */     if (rangeSlider.getMinorTickCount() != 0) {
/* 242 */       d = rangeSlider.getMajorTickUnit() / (Math.max(rangeSlider.getMinorTickCount(), 0) + 1);
/*     */     } else {
/* 244 */       d = rangeSlider.getMajorTickUnit();
/* 245 */     }  if (rangeSlider.getBlockIncrement() > 0.0D && rangeSlider.getBlockIncrement() < d) {
/* 246 */       return d;
/*     */     }
/* 248 */     return rangeSlider.getBlockIncrement();
/*     */   }
/*     */   
/*     */   private double snapValueToTicks(double d) {
/* 252 */     RangeSlider rangeSlider = (RangeSlider)getControl();
/* 253 */     double d1 = d;
/* 254 */     double d2 = 0.0D;
/* 255 */     if (rangeSlider.getMinorTickCount() != 0) {
/* 256 */       d2 = rangeSlider.getMajorTickUnit() / (Math.max(rangeSlider.getMinorTickCount(), 0) + 1);
/*     */     } else {
/* 258 */       d2 = rangeSlider.getMajorTickUnit();
/* 259 */     }  int i = (int)((d1 - rangeSlider.getMin()) / d2);
/* 260 */     double d3 = i * d2 + rangeSlider.getMin();
/* 261 */     double d4 = (i + 1) * d2 + rangeSlider.getMin();
/* 262 */     d1 = Utils.nearest(d3, d1, d4);
/* 263 */     return Utils.clamp(rangeSlider.getMin(), d1, rangeSlider.getMax());
/*     */   }
/*     */ 
/*     */   
/*     */   public void highThumbReleased(MouseEvent e) {
/* 268 */     RangeSlider slider = (RangeSlider)getControl();
/* 269 */     slider.setHighValueChanging(false);
/* 270 */     if (slider.isSnapToTicks())
/* 271 */       slider.setHighValue(snapValueToTicks(slider.getHighValue())); 
/*     */   }
/*     */   
/*     */   public void highThumbPressed(MouseEvent e, double position) {
/* 275 */     RangeSlider slider = (RangeSlider)getControl();
/* 276 */     if (!slider.isFocused())
/* 277 */       slider.requestFocus(); 
/* 278 */     slider.setHighValueChanging(true);
/*     */   }
/*     */   
/*     */   public void highThumbDragged(MouseEvent e, double position) {
/* 282 */     RangeSlider slider = (RangeSlider)getControl();
/* 283 */     slider.setHighValue(Utils.clamp(slider.getMin(), position * (slider.getMax() - slider.getMin()) + slider.getMin(), slider.getMax()));
/*     */   }
/*     */   
/*     */   public void moveRange(double position) {
/* 287 */     RangeSlider slider = (RangeSlider)getControl();
/* 288 */     double min = slider.getMin();
/* 289 */     double max = slider.getMax();
/* 290 */     double lowValue = slider.getLowValue();
/* 291 */     double newLowValue = Utils.clamp(min, lowValue + position * (max - min) / (
/* 292 */         (slider.getOrientation() == Orientation.HORIZONTAL) ? slider.getWidth() : slider.getHeight()), max);
/* 293 */     double highValue = slider.getHighValue();
/* 294 */     double newHighValue = Utils.clamp(min, highValue + position * (max - min) / (
/* 295 */         (slider.getOrientation() == Orientation.HORIZONTAL) ? slider.getWidth() : slider.getHeight()), max);
/*     */     
/* 297 */     if (newLowValue <= min || newHighValue >= max)
/* 298 */       return;  slider.setLowValueChanging(true);
/* 299 */     slider.setHighValueChanging(true);
/* 300 */     slider.setLowValue(newLowValue);
/* 301 */     slider.setHighValue(newHighValue);
/*     */   }
/*     */   
/*     */   public void confirmRange() {
/* 305 */     RangeSlider slider = (RangeSlider)getControl();
/*     */     
/* 307 */     slider.setLowValueChanging(false);
/* 308 */     if (slider.isSnapToTicks()) {
/* 309 */       slider.setLowValue(snapValueToTicks(slider.getLowValue()));
/*     */     }
/* 311 */     slider.setHighValueChanging(false);
/* 312 */     if (slider.isSnapToTicks())
/* 313 */       slider.setHighValue(snapValueToTicks(slider.getHighValue())); 
/*     */   }
/*     */   
/*     */   public static class RangeSliderKeyBinding
/*     */     extends OrientedKeyBinding
/*     */   {
/*     */     public RangeSliderKeyBinding(KeyCode code, String action) {
/* 320 */       super(code, action);
/*     */     }
/*     */     
/*     */     public RangeSliderKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
/* 324 */       super(code, type, action);
/*     */     }
/*     */     
/*     */     public boolean getVertical(Control control) {
/* 328 */       return (((RangeSlider)control).getOrientation() == Orientation.VERTICAL);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum FocusedChild {
/* 333 */     LOW_THUMB,
/* 334 */     HIGH_THUMB,
/* 335 */     RANGE_BAR,
/* 336 */     NONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\behavior\RangeSliderBehavior.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
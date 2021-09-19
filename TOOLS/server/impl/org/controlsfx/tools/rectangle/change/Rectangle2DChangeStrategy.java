package impl.org.controlsfx.tools.rectangle.change;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public interface Rectangle2DChangeStrategy {
  Rectangle2D beginChange(Point2D paramPoint2D);
  
  Rectangle2D continueChange(Point2D paramPoint2D);
  
  Rectangle2D endChange(Point2D paramPoint2D);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\rectangle\change\Rectangle2DChangeStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
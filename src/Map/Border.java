package Map;

import Geometry.Point;
import Geometry.Rectangle;
import Graphics.Window;

public class Border {
    private float offLeft, offRight, offTop, offBottom;

    public Border(float offLeft, float offRight, float offTop, float offBottom) {
        this.offLeft = (-Window.WINDOW_WIDTH / 2) + offLeft;
        this.offRight = (Window.WINDOW_WIDTH / 2) - offRight;
        this.offTop = (Window.WINDOW_HEIGHT / 2) - offTop;
        this.offBottom = (-Window.WINDOW_HEIGHT / 2) + offBottom;
    }

    private float checkX(float x, float width) {
        if (x <= offLeft) {
            return offLeft + 1f;
        }

        if (x + width >= offRight) {
            return offRight - width - 1f;
        }

        return x;
    }

    private float checkY(float y, float height) {
        if (y >= offTop) {
            return offTop - 1f;
        }

        if (y - height <= offBottom) {
            return offBottom + height + 1f;
        }

        return y;
    }

    public boolean atBorder(Rectangle rect) {
        float x = rect.getPosition().getX(), y = rect.getPosition().getY();
        return x <= offLeft || x + rect.getWidth() >= offRight || y >= offTop || y - rect.getHeight() <= offBottom;
    }

    public Point recomputePosition(Rectangle rect) {
        Point result = rect.getPosition();
        return new Point(checkX(result.getX(), rect.getWidth()), checkY(result.getY(), rect.getHeight()));
    }
}

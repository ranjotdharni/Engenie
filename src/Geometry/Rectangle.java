package Geometry;

public class Rectangle {
    private float width, height, scale;
    private Point position;

    public Rectangle(Point position, float width, float height, float scale) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.scale = scale;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Point getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getScale() {
        return scale;
    }
}

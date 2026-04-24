package Render;

import Geometry.Point;
import Geometry.Rectangle;
import Orchestration.Window;

public class QuadVertexMap {
    private float normalizedWindowWidthUnit = 0.0f;
    private float normalizedWindowHeightUnit = 0.0f;

    private float x = 0.0f;
    private float y = 0.0f;
    private float width = 0.0f;
    private float height = 0.0f;
    private float scale = 1.0f;

    private boolean flipX = false;
    private boolean flipY = false;

    public QuadVertexMap(float windowWidth, float windowHeight, Rectangle canvas, float scale) {
        this.normalizedWindowWidthUnit = 2f / windowWidth;
        this.normalizedWindowHeightUnit = 2f / windowHeight;

        Point position = canvas.getPosition();

        this.x = position.getX();
        this.y = position.getY();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.scale = scale;
    }

    public QuadVertexMap(float windowWidth, float windowHeight, Rectangle canvas, float scale, boolean flipX, boolean flipY) {
        this.normalizedWindowWidthUnit = 2f / windowWidth;
        this.normalizedWindowHeightUnit = 2f / windowHeight;

        Point position = canvas.getPosition();

        this.x = position.getX();
        this.y = position.getY();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.scale = scale;

        this.flipX = flipX;
        this.flipY = flipY;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public float calculateTop() {
        if (this.flipY)
            return ( this.y * this.normalizedWindowHeightUnit ) - 1f;

        return ( ( this.y + ( this.height * this.scale ) ) * this.normalizedWindowHeightUnit ) - 1f;
    }

    public float calculateBottom() {
        if (this.flipY)
            return ( ( this.y + ( this.height * this.scale ) ) * this.normalizedWindowHeightUnit ) - 1f;

        return ( this.y * this.normalizedWindowHeightUnit ) - 1f;
    }

    public float calculateRight() {
        if (this.flipX)
            return ( this.x * this.normalizedWindowWidthUnit ) - 1f;

        return ( ( this.x + ( this.width * this.scale ) ) * this.normalizedWindowWidthUnit ) - 1f;
    }

    public float calculateLeft() {
        if (this.flipX)
            return ( ( this.x + ( this.width * this.scale ) ) * this.normalizedWindowWidthUnit ) - 1f;

        return ( this.x * this.normalizedWindowWidthUnit ) - 1f;
    }
}

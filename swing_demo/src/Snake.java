public class Snake {
    final static int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    int x, y, direction, nextDirection;

    public Snake(int _x, int _y, int _direction) {
        x = _x;
        y = _y;
        direction = _direction;
        nextDirection = direction;
    }
}

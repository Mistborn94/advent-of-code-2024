package helper.point

import helper.point.base.Point

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

/**
 * Direction with up being negative and
 */
enum class Direction(val char: Char, val orientation: Orientation) {

    UP('^', Orientation.VERTICAL) {
        override val left get() = LEFT
        override val right get() = RIGHT
        override val opposite get() = DOWN
    },
    RIGHT('>', Orientation.HORIZONTAL) {
        override val left get() = UP
        override val right get() = DOWN
        override val opposite get() = LEFT
    },
    DOWN('v', Orientation.VERTICAL) {
        override val left get() = RIGHT
        override val right get() = LEFT
        override val opposite get() = UP
    },
    LEFT('<', Orientation.HORIZONTAL) {
        override val left get() = DOWN
        override val right get() = UP
        override val opposite get() = RIGHT
    };

    abstract val left: Direction
    abstract val right: Direction
    abstract val opposite: Direction

    val point: Point get() = DirectionPointMapping.downPositive[this]
    val pointPositiveUp: Point get() = DirectionPointMapping.upPositive[this]

    override fun toString(): String = "$char"

    companion object {
        val NORTH = UP
        val EAST = RIGHT
        val SOUTH = DOWN
        val WEST = LEFT

        fun parse(c: Char) = when (c) {
            '<' -> Direction.LEFT
            '>' -> Direction.RIGHT
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            else -> throw IllegalArgumentException("Unknown direction $c")
        }

    }
}

private class DirectionPointMapping(positiveY: Direction, positiveX: Direction) {

    val up = if (positiveY == Direction.UP) Point(0, 1) else Point(0, -1)
    val down = Point(0, -up.y)
    val left = if (positiveX == Direction.LEFT) Point(1, 0) else Point(-1, 0)
    val right = Point(-left.x, 0)

    operator fun get(direction: Direction): Point {
        return when (direction) {
            Direction.UP -> up
            Direction.DOWN -> down
            Direction.LEFT -> left
            Direction.RIGHT -> right
        }
    }

    companion object {
        val downPositive = DirectionPointMapping(Direction.DOWN, Direction.RIGHT)
        val upPositive = DirectionPointMapping(Direction.UP, Direction.RIGHT)
    }
}
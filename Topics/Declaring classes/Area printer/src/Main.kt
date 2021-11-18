class Rectangle(val width: Int, val height: Int)

fun printArea(rectangle: Rectangle) {
    var area = rectangle.width * rectangle.height
    print( area)
}
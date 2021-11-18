fun main() {
    val a = readLine()!!.toInt()
    val b = readLine()!!.toInt()

    var p: Long = 1

    for (i in a until b)
        p *= i

    println(p)
}
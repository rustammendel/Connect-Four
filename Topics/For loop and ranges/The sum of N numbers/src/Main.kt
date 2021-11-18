fun main() {
    val a = readLine()!!.toInt()
    var sum = 0
    for (i in 1..a) {
        sum += readLine()!!.toInt()
    }
    println(sum)
}
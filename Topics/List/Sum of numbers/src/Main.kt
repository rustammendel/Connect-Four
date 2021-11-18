fun solution(numbers: List<Int>): Int {
    var sum = 0
    numbers.forEach{sum += it}
    return sum
}
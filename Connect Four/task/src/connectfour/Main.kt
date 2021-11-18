package connectfour


class Board {
    var row = 6
    var col = 7
    var grid = mutableListOf<MutableList<Char>>()

    var pl1: String = ""
    var pl2: String = ""
    var pl1s = 0
    var pl2s = 0

    var turn1: Boolean = true
    var terminate = false
    var winsit = false
    var isdraw = false
    var leftrun = 0

    fun getPlayerNames() {

        println("First player's name:")
        pl1 = readLine()!!

        println("Second player's name:")
        pl2 = readLine()!!
    }

    fun setDimension() {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")

        var dimension: String? = readLine()
        if (!dimension.isNullOrEmpty()) {
            dimension = dimension.filter { !it.isWhitespace() }
            val inpFormat = """(\d+)[xX](\d+)""".toRegex()
            val isGood = inpFormat.matchEntire(dimension)
            if (isGood == null) {
                println("Invalid input")
                setDimension()
            } else {
                row = isGood.groups[1]!!.value.toInt()
                col = isGood.groups[2]!!.value.toInt()
            }


            if (row !in 5..9) {
                println("Board rows should be from 5 to 9")
                setDimension()
            } else if (col !in 5..9) {
                println("Board columns should be from 5 to 9")
                setDimension()
            }
        }

        for (i in 0 until row) {
            val arow = mutableListOf<Char>()
            for (j in 0 until col) {
                arow.add(' ')
            }
            grid.add(arow)

        }
    }

    fun dumpGrid() {
        println(grid)
    }

    fun resetGrid() {
        for (i in 0 until row) {
            for (j in 0 until col) {
                grid[i][j] = (' ')
            }
        }
    }

    fun drawBoard() {

        for (i in 1..col) {
            print(" $i")
        }
        println()
        for (i in 0 until row) {
            for (j in 0 until col)
                print("║${grid[i][j]}")
            print("║")
            println()
        }
        print("╚")

        for (i in 1 until col) {
            print("═╩")
        }
        println("═╝")

    }

    fun askCol() {
        println((if (turn1) pl1 else pl2) + "'s turn: ")
        val inCol = readLine()
        try {
            if (inCol == "end") {
                println("Game over!")
                terminate = true

            } else {
                val colint = inCol!!.toInt()
                if (colint in (1..col)) {
                    putDisk(if (turn1) 'o' else '*', colint)
                } else if (colint !in (1..col)) {
                    println("The column number is out of range (1 - $col)")
                    askCol()
                }
            }
        } catch (e: Exception) {
            println("Incorrect column number")
            askCol()
        }

    }

    fun putDisk(disc: Char, col: Int) {

        //Look for the first empty row from bottom
        var erow = row - 1
        while (erow >= 0 && grid[erow][col - 1] != ' ') {
            erow--
        }
        if (erow >= 0) {
            grid[erow][col - 1] = disc
            leftrun++
            evaluate(erow, col - 1)
            turn1 = !turn1
        } else {
            println("Column $col is full ")
            askCol()
        }

    }

    fun inGrid(r: Int, c: Int): Boolean {
        val rok = r in 0 until row
        val cok = c in 0 until col
        return rok && cok
    }

    fun evaluate(_row: Int, _col: Int) {
        if (leftrun == col * row) {
            //grid full
//            println("grid full")
            isdraw = true
            terminate = true
            return
        }


        val dsc = grid[_row][_col]
        val rd = listOf(-1, 1, 0, 0, 1, -1, 1, -1)
        val cd = listOf(0, 0, -1, 1, -1, 1, 1, -1)
        var cnt = 1

        for (i in 0 until 8) {
            var ri = 1
            var ci = 1
            while (inGrid(
                    _row + (rd[i] * ri),
                    _col + (cd[i] * ci)
                ) && grid[_row + (rd[i] * ri++)][_col + (cd[i] * ci++)] == dsc
            ) {
                cnt++
            }

            if (cnt == 4) {
                winsit = true
                terminate = true
                return
            } else {
                cnt = 1
                continue
            }
        }

    }

}


fun setGameMode(): Int {
    println(
        "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
    )
    var gmode = 1
    val gm: String? = readLine()
    if (!gm.isNullOrEmpty()) {
        val inpFormat = """(\d+)""".toRegex()
        val isGood = inpFormat.matchEntire(gm)
        if (isGood == null || gm == "0") {
            println("Invalid input")
            setGameMode()
        } else {
            gmode = gm.toInt()
        }
    }
    return gmode
}

fun startgame(brd: Board) {
    brd.drawBoard()

    while (!brd.terminate) {
        brd.askCol()
        brd.drawBoard()
    }
    if (brd.winsit) {
        println("Player ${if (!brd.turn1) brd.pl1 else brd.pl2} won")
        if (!brd.turn1) brd.pl1s += 2 else brd.pl2s = +2
    }
    if (brd.isdraw) {
        println("It is a draw")
        brd.pl1s += 1
        brd.pl2s += 1
    }
}

fun resetBrd(brd: Board) {
    brd.terminate = false
    brd.isdraw = false
    brd.leftrun = 0
    brd.resetGrid()
}


fun main() {
    println("Connect Four")
    val brd = Board()
    brd.getPlayerNames()
    brd.setDimension()
    val multiMode = setGameMode()

    println("${brd.pl1} VS ${brd.pl2}")
    println("${brd.row} X ${brd.col} board")
    println(if (multiMode == 1) "Single game" else "Total $multiMode games")

    var currGame = 0
    while (++currGame <= multiMode) {
        if (multiMode > 1) println("Game #${currGame}")
        startgame(brd)
        if (multiMode > 1) {
            println("Score")
            println("${brd.pl1}: ${brd.pl1s} ${brd.pl2}: ${brd.pl2s}")
        }
        resetBrd(brd)
    }
    println("Game over!")
}
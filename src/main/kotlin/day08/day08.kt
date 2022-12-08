package day08

import utitlities.readStringFile

fun main() {
    println(visibleTrees("src/main/kotlin/day08/test.txt"))
    println(visibleTrees("src/main/kotlin/day08/input.txt"))

    println(maxSenicScore("src/main/kotlin/day08/test.txt"))
    println(maxSenicScore("src/main/kotlin/day08/input.txt"))
}

fun visibleTrees(fileName: String): Int {
    val input = readStringFile(fileName)
    val grid = initTreeGrid(input)
    return countVisibleTrees(grid.first)
}

fun maxSenicScore(fileName: String): Int {
    val input = readStringFile(fileName)
    val grid = initTreeGrid(input)
    return grid.first.flatMap { it.trees }.map { it.scenicScore() }.max()
}

fun countVisibleTrees(rows: ArrayList<Row>): Int {
    val visibleTrees = rows.flatMap { it.trees }.filter { it.isVisible() }.count()
    return visibleTrees
}

fun initTreeGrid(lines: List<String>): Pair<ArrayList<Row>, ArrayList<Column>> {
    var i = 0
    val rows: ArrayList<Row> = ArrayList()
    val columns: ArrayList<Column> = ArrayList()

    do {
        val trees = lines[i].toCharArray()
        if (i <= rows.size) {
            rows.add(Row(i))
        }
        val row = rows.get(i)

        var j = 0
        do {
            if (j <= columns.size) {
                columns.add(Column(j))
            }
            val column = columns.get(j)

            val tree = Tree(row, i, column, j, lines[i].get(j).digitToInt())
            row.trees.add(tree)
            column.trees.add(tree)
            j++
        } while (j < trees.size)
        i++
    } while (i < lines.size)

    return Pair(rows, columns)
}

class Row(val number: Int, val trees: ArrayList<Tree> = ArrayList())
class Column(val number: Int, val trees: ArrayList<Tree> = ArrayList())

class Tree(val row: Row, val rowIndex: Int, val column: Column, val columnIndex: Int, val height: Int) {
    fun isVisible(): Boolean {
        return rowIndex == 0
                || rowIndex == row.trees.size - 1
                || columnIndex == 0
                || columnIndex == column.trees.size - 1
                || allSmaller(row.trees, columnIndex)
                || allSmaller(column.trees, rowIndex)
    }

    fun scenicScore(): Int {
        return scenicScore(row.trees, columnIndex) * scenicScore(column.trees, rowIndex)
    }

    private fun allSmaller(collection: ArrayList<Tree>, index: Int): Boolean {
        val before = collection.subList(0, index)
        val after = collection.subList(index + 1, collection.size)
        return !(before.map { it.height }.filter { it >= this.height }.size > 0)
                || !(after.map { it.height }.filter { it >= this.height }.size > 0)
    }

    private fun scenicScore(collection: ArrayList<Tree>, index: Int): Int {
        val before = collection.subList(0, index)

        var score1 = 0
        if (index > 0) {
            for (i in index - 1 downTo 0) {
                score1++
                if (before[i].height >= height)
                    break
            }
        }

        var score2 = 0
        if (index < collection.size) {
            for (i in index + 1..collection.size - 1) {
                score2++
                if (collection[i].height >= height)
                    break
            }
        }
        return score1 * score2
    }

}
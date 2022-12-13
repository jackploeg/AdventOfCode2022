package day13

import utitlities.readStringFile
import java.lang.Math.min


fun main() {

    println(checkPacketOrder("src/main/kotlin/day13/test.txt"))
    println(checkPacketOrder("src/main/kotlin/day13/input.txt"))

    println(orderPackets("src/main/kotlin/day13/test.txt"))
    println(orderPackets("src/main/kotlin/day13/input.txt"))

}

fun checkPacketOrder(fileName: String): Int {
    val input = readStringFile(fileName)

    var left: ArrayList<Any>? = null
    var right: ArrayList<Any>?
    var counter = 1
    val packetPairs: ArrayList<PacketPair> = arrayListOf()
    input.filter { it.length > 0 }.forEach {
        if (left == null) {
            left = parseArray(it)
        } else {
            right = parseArray(it)
            packetPairs.add(PacketPair(counter++, left!!, right!!))
            left = null
            right = null
        }

    }

    return packetPairs.filter { orderCorrect(it.left, it.right) ?: false }.map { it.index }.sum()
}

fun orderPackets(fileName: String): Int {
    val input = readStringFile(fileName)
    val packets: ArrayList<ArrayList<Any>> = arrayListOf()

    input.filter { it.length > 0 }.forEach {
        packets.add(parseArray(it))
    }

    // add markers
    val marker1 = parseArray("[[2]]")
    val marker2 = parseArray("[[6]]")
    packets.add(marker1)
    packets.add(marker2)

    packets.sortWith { o1, o2 -> if (orderCorrect(o1, o2) == true) -1 else 1 }

    var response = 1
    // find markers
    for (i in 1..packets.size) {
        if (packets[i - 1] == marker1 || packets[i - 1] == marker2) {
            response *= i
        }
    }
    return response
}

fun parseArray(input: String): ArrayList<Any> {
    val result: ArrayList<Any> = arrayListOf()
    // strip [ and ]
    val content = input.substring(1, input.length - 1)
    var element: String
    var i = 0
    while (i <= content.length - 1) {
        var char = content[i]
        element = ""
        if (char == '[') {
            var bracketLevel = 0
            while ((bracketLevel > 0 || char == '[') && i <= content.length) {
                element = element.plus(char)
                when (char) {
                    '[' -> bracketLevel++
                    ']' -> bracketLevel--
                }
                i++
                char = content[min(i, content.length - 1)]
            }
            result.add(parseArray(element))
        } else {
            element = ""
            while (char != ',' && char != ']' && i <= content.length - 1) {
                element = element.plus(char)
                i++
                char = content[min(i, content.length - 1)]
            }
            result.add(Integer.parseInt(element))
        }
        i++
    }
    return result
}

class PacketPair(val index: Int, val left: ArrayList<Any>, val right: ArrayList<Any>)

@Suppress("UNCHECKED_CAST")
fun orderCorrect(left: ArrayList<Any>, right: ArrayList<Any>): Boolean? {
    val maxElement = min(left.size, right.size) - 1
    var result: Boolean?

    for (i in 0..maxElement) {
        if (left[i] is Int && right[i] is Int) {
            if ((left[i] as Int) < (right[i] as Int)) {
                return true
            } else if ((right[i] as Int) < (left[i] as Int)) {
                return false
            }
        }
        if (left[i] is List<*> && right[i] is Int) {
            result = orderCorrect(left[i] as ArrayList<Any>, arrayListOf(right[i]))
            if (result != null)
                return result
        }
        if (left[i] is Int && right[i] is List<*>) {
            result = orderCorrect(arrayListOf(left[i]), right[i] as ArrayList<Any>)
            if (result != null)
                return result
        }
        if (left[i] is List<*> && right[i] is List<*>) {
            result = orderCorrect(left[i] as ArrayList<Any>, right[i] as ArrayList<Any>)
            if (result != null)
                return result
        }
    }

    return if (left.size == right.size) null
    else left.size <= right.size
}
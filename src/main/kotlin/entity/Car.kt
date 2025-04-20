package entity

data class Car (
    val name: String,
    var distance: Int,
) {
    fun move(): Int {
        val random = (0..9).random()
        if (random >= 4) {
            distance++
        }
        return distance
    }

    fun getStatus(): String {
        val sb = StringBuilder()
        sb.append(name)
        sb.append(" : ")
        repeat(distance) {
            sb.append("-")
        }
        return sb.toString()
    }
}
package entity

data class Car (
    val name: String,
    var distance: Int,
) {
    fun move() {
        distance++
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
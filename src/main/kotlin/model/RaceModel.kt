package model

import entity.Car

class RaceModel {
    var carList: List<Car> = emptyList()

    fun initCarList(names: String): List<Car> {
        if (names.isEmpty()) throw IllegalArgumentException("입력된 이름이 없습니다.")
        carList = names.split(",").map {
            if (it.length > 5) throw IllegalArgumentException("자동차 이름은 5자 이하만 가능합니다.")
            Car(it, 0)
        }
        return carList
    }
}
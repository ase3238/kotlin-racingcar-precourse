package controller

import model.RaceModel
import view.RaceView

class RaceController (
    val raceModel: RaceModel,
    val raceView: RaceView,
) {
    fun runGame() {

    }

    private fun initCarList() {
        while (raceModel.carList.isEmpty()) {
            try {
                raceView.showCarInitMsg()
                raceModel.initCarList(readln())
            } catch (e: IllegalArgumentException) {
                handleError(e)
            }
        }
    }

    private fun handleError(e: Exception) {
        raceView.showErrorMsg(e.message.toString())
    }
}
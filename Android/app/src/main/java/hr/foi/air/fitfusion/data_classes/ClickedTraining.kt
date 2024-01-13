package hr.foi.air.fitfusion.data_classes

import android.content.Context
import android.content.SharedPreferences

class ClickedTraining(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ClickedTraining", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val KEY_TRAINING_ID = "training_id"
        const val KEY_TRAINING_DATE = "date"
        const val KEY_TRAINING_TIME = "time"
        const val KEY_PARTICIPANTS = "participants"
        const val KEY_TRAINING_TYPE = "type"
    }

    fun saveTrainingData(trainingId: String?, date: String?, time: String?, participants: String?, type: String?) {
        editor.putString(KEY_TRAINING_ID, trainingId)
        editor.putString(KEY_TRAINING_DATE, date)
        editor.putString(KEY_TRAINING_TIME, time)
        editor.putString(KEY_PARTICIPANTS, participants)
        editor.putString(KEY_TRAINING_TYPE, type)
        editor.apply()
    }

    fun getTrainingId(): String? {
        return sharedPreferences.getString(KEY_TRAINING_ID, null)
    }

    fun getTrainingDate(): String? {
        return sharedPreferences.getString(KEY_TRAINING_DATE, null)
    }
    fun getTrainingTime(): String? {
        return sharedPreferences.getString(KEY_TRAINING_TIME, null)
    }
    fun getParticipants(): String? {
        return sharedPreferences.getString(KEY_PARTICIPANTS, null)
    }
    fun getTrainingType(): String? {
        return sharedPreferences.getString(KEY_TRAINING_TYPE, null)
    }

    fun clearTrainingData() {
        editor.remove(KEY_TRAINING_ID)
        editor.remove(KEY_TRAINING_DATE)
        editor.remove(KEY_TRAINING_TIME)
        editor.remove(KEY_PARTICIPANTS)
        editor.remove(KEY_TRAINING_TYPE)

        editor.apply()
    }
}
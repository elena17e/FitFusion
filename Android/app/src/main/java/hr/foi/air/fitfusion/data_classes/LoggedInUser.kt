package hr.foi.air.fitfusion.data_classes
import android.content.Context
import android.content.SharedPreferences

class LoggedInUser(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("LoggedInUser", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_FIRST_NAME = "first_name"
        const val KEY_LAST_NAME = "last_name"
        const val KEY_PASSWORD = "password"
        const val KEY_EMAIL = "email"
        const val KEY_TYPE = "type"

    }

    fun saveUserData(userId: String?, firstName: String?, lastName: String?, password: String, email: String, type: String?) {
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.putString(KEY_LAST_NAME, lastName)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_TYPE, type)

        editor.apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getFirstName(): String? {
        return sharedPreferences.getString(KEY_FIRST_NAME, null)
    }
    fun getLastName(): String? {
        return sharedPreferences.getString(KEY_LAST_NAME, null)
    }
    fun getPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }
    fun getEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }
    fun getType(): String? {
        return sharedPreferences.getString(KEY_TYPE, null)
    }

    fun clearUserData() {
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_FIRST_NAME)
        editor.remove(KEY_LAST_NAME)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_TYPE)

        editor.apply()
    }

}
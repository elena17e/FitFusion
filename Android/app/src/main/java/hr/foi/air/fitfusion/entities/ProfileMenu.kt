package hr.foi.air.fitfusion.entities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import hr.foi.air.fitfusion.LoginActivity2
import hr.foi.air.fitfusion.R
import hr.foi.air.fitfusion.data_classes.LoggedInUser

object ProfileMenu {
    fun showMenu(context: Context, anchor: View, menuRes: Int, actionHandler: (Int) -> Unit){
        val popup = PopupMenu(context, anchor)
        popup.menuInflater.inflate(menuRes, popup.menu)

        try {
            val fields = android.widget.PopupMenu::class.java.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("Popup", "Error showing menu icons", e)
        }

        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            actionHandler(menuItem.itemId)
            true
        }
        popup.show()
    }

    fun handleLogout(activity: AppCompatActivity, loggedInUser: LoggedInUser) {
        loggedInUser.clearUserData()
        val intent = Intent(activity, LoginActivity2::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
        activity.finish()
    }
}
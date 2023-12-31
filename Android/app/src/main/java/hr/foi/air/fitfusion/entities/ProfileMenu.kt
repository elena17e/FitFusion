package hr.foi.air.fitfusion.entities

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import hr.foi.air.fitfusion.R

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
}
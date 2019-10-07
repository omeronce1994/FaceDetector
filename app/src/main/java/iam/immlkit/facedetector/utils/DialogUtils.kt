package iam.immlkit.facedetector.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class DialogUtils {

    companion object{

        fun showAlertDialog(context: Context,title:String,message:String, iconRes:Int){
            val builder = AlertDialog.Builder(context)
            builder
                .setTitle(title)
                .setMessage(message)
                .setIcon(iconRes)
                .setCancelable(false)
                .setPositiveButton("OK",{
                    dialogInterface, i -> dialogInterface.dismiss()
                })
                .show()
        }
    }
}
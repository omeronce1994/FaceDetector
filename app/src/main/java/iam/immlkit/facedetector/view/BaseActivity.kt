package iam.immlkit.facedetector.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabriel.chief.Managers.AppActivityManager

abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //register for activity manager
        AppActivityManager.instance.registerOwner(this)
    }
}
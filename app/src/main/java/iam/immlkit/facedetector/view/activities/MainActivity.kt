package iam.immlkit.facedetector.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import iam.immlkit.facedetector.R
import iam.immlkit.facedetector.model.ServiceLocator
import iam.immlkit.facedetector.model.rxbus.LiveDataEventBus
import iam.immlkit.facedetector.model.rxbus.RxBus
import iam.immlkit.facedetector.model.rxbus.events.OnFinishedDetectionWithDialog
import iam.immlkit.facedetector.utils.DialogUtils
import iam.immlkit.facedetector.utils.PermissionsUtils
import iam.immlkit.facedetector.view.pager.SectionsPagerAdapter
import iam.immlkit.facedetector.view.BaseActivity
import iam.immlkit.facedetector.view.allimages.AllImagesFragment
import iam.immlkit.facedetector.view.faceimages.FaceImagesFragment
import iam.immlkit.facedetector.view.nonfacesimages.NonFacesImagesFragment
import iam.immlkit.facedetector.viewmodel.detection.DetectionViewModel
import iam.immlkit.facedetector.viewmodel.detection.DetectionViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import rx.functions.Action1

class MainActivity : BaseActivity() {

    companion object {

        val TAG = "MainActivity"

        val RQ_PERMISSIONS = 101
    }

    private val viewModel: DetectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPager()
        listenForEvents()
        checkForPermissionCheck()
    }

    //We could listen to it view model but since this event fired when we know we are in foreground we can fire it here
    private fun listenForEvents() {
        //show dialog when face detection is finished
        RxBus.subscribe(TAG,OnFinishedDetectionWithDialog::class.java, object : Action1<OnFinishedDetectionWithDialog>{
            override fun call(event: OnFinishedDetectionWithDialog?) {
                val faceCount = event?.faceCount ?:0
                val total = event?.totalDetected ?:0
                val text = getString(R.string.format_detction_results_text,faceCount,total)
                showFinishDetectionDialog(text)
            }
        })
        LiveDataEventBus.subscribe(this,String::class.java){
            tv_toolbar_title.text = it
        }
    }

    private fun showFinishDetectionDialog(message:String){
        DialogUtils.showAlertDialog(this,getString(R.string.app_name),message,R.mipmap.ic_launcher)
    }

    private fun initPager() {
        val titles = listOf(getString(R.string.tab_title_all),getString(R.string.tab_title_faces),getString(
            R.string.tab_title_non_faces
        ))
        val fragments = listOf(AllImagesFragment.newInstance(),FaceImagesFragment.newInstance(),NonFacesImagesFragment.newInstance())
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager,fragments, titles)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }

    /**
     * Check if runtime permission check is needed before accessing to Disk
     */
    private fun checkForPermissionCheck(){
        if(PermissionsUtils.isPermissionCheckNeeded())
            permissionCheck()
        else
            onPermissionGranted()
    }

    /**
     * Check for permissions if not granted already
     */
    private fun permissionCheck(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
        Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                RQ_PERMISSIONS
            )
        } else {
            onPermissionGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RQ_PERMISSIONS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    onPermissionGranted()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    /**
     * Function to perform operations that needed runtime permission, thus call this function only after permissions are granted
     */
    private fun onPermissionGranted(){
        //set on click listener for button only after we are granted premission
        btn_detect.setOnClickListener{
            viewModel.detect(this)
        }
        btn_clear.setOnClickListener {
            viewModel.clearAllTables()
        }
        viewModel.loadFromDisk()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.removeListener(TAG)
    }
}
package com.mobile.fodein.presentation.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import butterknife.ButterKnife
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ActivityModule
import com.mobile.fodein.presentation.view.component.AuthenticateAdapter
import com.mobile.fodein.presentation.view.component.ManageImages
import com.mobile.fodein.tools.PermissionUtils
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class AccessActivity: AppCompatActivity() {

    var image: ImageView? = null
    var observableImage: Subject<ImageView> = PublishSubject.create()

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.getAppComponent()
            .plus(ActivityModule(this))}

    @Inject
    lateinit var manageImages: ManageImages
    @Inject
    lateinit var permissionUtils: PermissionUtils

    init {
        observableImage
                .subscribe { image }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access)
        component.inject(this)
        image = ImageView(this)
        val pager: ViewPager = ButterKnife.findById(this, R.id.vp_access)
        pager.adapter = AuthenticateAdapter(supportFragmentManager, pager)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == manageImages.GALLERY_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK && data != null) {
            manageImages.setControlImage(data.data, image!!)
            this.observableImage.onNext(this.image!!)
        } else if (requestCode == manageImages.CAMERA_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            val photoUri = FileProvider.getUriForFile(this,
                    applicationContext.packageName +
                            ".provider", manageImages.getCameraFile())
            manageImages.setControlImage(photoUri, image!!)
            this.observableImage.onNext(this.image!!)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            manageImages.CAMERA_PERMISSIONS_REQUEST ->
                if (permissionUtils.permissionGranted(requestCode,
                        manageImages.CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    manageImages.startCamera()
            }
            manageImages.GALLERY_PERMISSIONS_REQUEST ->
                if (permissionUtils.permissionGranted(requestCode,
                        manageImages.GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    manageImages.startGalleryChooser()
            }
        }
    }

    fun camera(){
        manageImages.startCamera()
    }

    fun gallery(){
        manageImages.startGalleryChooser()
    }

    fun convertBase64(bitmap: Bitmap): String = manageImages.base641EncodedImage(bitmap)

    fun convertBitmap(baseString: String): Bitmap = manageImages.base64DecodeImage(baseString)
}
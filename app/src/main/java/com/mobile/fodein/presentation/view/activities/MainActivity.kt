package com.mobile.fodein.presentation.view.activities

class MainActivity : BaseActivity() {
    //    @BindView(R.id.iv_place)
//    @JvmField var ivPlace: ImageView? = null
//
//    @OnClick(R.id.bt_camera)
//    fun camera(){
//        manageImages.startCamera()
//    }
//    @OnClick(R.id.bt_gallery)
//    fun gallery(){
//        manageImages.startGalleryChooser()
//    }
    override fun onStart() {
        super.onStart()
        //manageMaps.getMapAsync()
//        ButterKnife.bind(this)

        //navigator.addFragment(R.id.fragmentContainer, UserListFragment())
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == manageImages.GALLERY_IMAGE_REQUEST &&
//                resultCode == Activity.RESULT_OK && data != null) {
//            manageImages.setControlImage(data.data, ivPlace!!)
//        } else if (requestCode == manageImages.CAMERA_IMAGE_REQUEST &&
//                resultCode == Activity.RESULT_OK) {
//            val photoUri = FileProvider.getUriForFile(this,
//                    applicationContext.packageName +
//                            ".provider", manageImages.getCameraFile())
//            manageImages.setControlImage(photoUri, ivPlace!!)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<out String>,
//                                            grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            manageImages.CAMERA_PERMISSIONS_REQUEST ->
//                if (permissionUtils.permissionGranted(requestCode,
//                        manageImages.CAMERA_PERMISSIONS_REQUEST, grantResults)) {
//                    manageImages.startCamera()
//            }
//            manageImages.GALLERY_PERMISSIONS_REQUEST ->
//                if (permissionUtils.permissionGranted(requestCode,
//                        manageImages.GALLERY_PERMISSIONS_REQUEST, grantResults)) {
//                    manageImages.startGalleryChooser()
//            }
//        }
//
//    }
}




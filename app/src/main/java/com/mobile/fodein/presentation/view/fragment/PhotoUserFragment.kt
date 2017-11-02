package com.mobile.fodein.presentation.view.fragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.activities.AccessActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class PhotoUserFragment: AuthenticateFragment() {
    @BindView(R.id.iv_photo)
    @JvmField var ivPhoto: ImageView? = null
    @OnClick(R.id.bt_camera)
    fun shootCamera(){
        (activity as AccessActivity).camera()
    }
    @OnClick(R.id.bt_gallery)
    fun viewGallery(){
        (activity as AccessActivity).gallery()
    }
    @OnClick(R.id.bt_save)
    fun saveImage(){
        if (userImage != null){
            imageBase64 = (activity as AccessActivity).convertBase64(userImage!!)
            if (!imageBase64.isEmpty()){
                context.toast(resources
                        .getString(R.string.save_data))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image = (activity as AccessActivity).observableImage.map { i -> i }
        disposable.add(image.observeOn(Schedulers.newThread())
                .map { i ->
                    kotlin.run {
                        val img: Bitmap = (i.drawable as BitmapDrawable).bitmap
                        return@map Bitmap.createScaledBitmap(img, (img.width*0.2).toInt(),
                                (img.height*0.2).toInt(), true)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { resize ->
                    kotlin.run {
                        userImage = resize
                        ivPhoto!!.setImageBitmap(resize)
                    }
                })
    }
    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_photo_user,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }

    override fun onStart() {
        super.onStart()
        if (userImage != null){
            ivPhoto!!.setImageBitmap(userImage)
        }

    }

}
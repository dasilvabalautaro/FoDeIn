package com.mobile.fodein.presentation.view.activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mobile.fodein.App
import com.mobile.fodein.R
import com.mobile.fodein.dagger.ActivityModule
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.domain.data.MapperImage
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.FormModel
import com.mobile.fodein.presentation.model.ImageModel
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.presenter.*
import com.mobile.fodein.presentation.view.component.ImageAdapter
import com.mobile.fodein.presentation.view.component.ManageImages
import com.mobile.fodein.tools.ConnectionNetwork
import com.mobile.fodein.tools.Constants
import com.mobile.fodein.tools.LocationUser
import com.mobile.fodein.tools.PermissionUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Cancellable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ILoadDataView {

    private var disposable: CompositeDisposable = CompositeDisposable()

    val Activity.app: App
        get() = application as App

    private val component by lazy { app.getAppComponent()
            .plus(ActivityModule(this))}

    @Inject
    lateinit var manageImages: ManageImages
    @Inject
    lateinit var permissionUtils: PermissionUtils
    @Inject
    lateinit var projectPresenter: ProjectPresenter
    @Inject
    lateinit var locationUser: LocationUser
    @Inject
    lateinit var formNewPresenter: FormNewPresenter
    @Inject
    lateinit var addFormProjectListPresenter: AddFormProjectListPresenter
    @Inject
    lateinit var formRegisterNetworkPresenter: FormRegisterNetworkPresenter
    @Inject
    lateinit var addImageListPresenter: AddImageListPresenter
    @Inject
    lateinit var formSelectPresenter: FormSelectPresenter
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork
    @Inject
    lateinit var imageListPresenter: ImageListPresenter

    private var idProject: String = ""
    private var idNetProject: String = ""
    var image: ImageView? = null
    private var listImages: ArrayList<ImageView> = ArrayList()
    private var listMapperImage: ArrayList<MapperImage> = ArrayList()
    private var listModel: List<ProjectModel> = ArrayList()
    private var dateFormatter: SimpleDateFormat =
            SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private val pack: MutableMap<String, Any> = HashMap()
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    val ID_FORM = "idForm"
    var idFormSelect = ""
    var adapter: ImageAdapter? = null
    var isWarnedToClose = false
    val ACTION_HOME = 16908332

    @BindView(R.id.rv_images)
    @JvmField var rvImages: RecyclerView? = null
    @BindView(R.id.tv_title)
    @JvmField var tvTitle: TextView? = null
    @BindView(R.id.et_day)
    @JvmField var etDay: EditText? = null
    @BindView(R.id.et_annotation)
    @JvmField var etAnnotation: EditText? = null
    @BindView(R.id.et_annotation_one)
    @JvmField var etAnnotationOne: EditText? = null
    @BindView(R.id.et_annotation_two)
    @JvmField var etAnnotationTwo: EditText? = null
    @BindView(R.id.sp_filter)
    @JvmField var spFilter: Spinner? = null
    @BindView(R.id.ib_save_form)
    @JvmField var ibSaveForm: ImageButton? = null
    @BindView(R.id.ib_save_image)
    @JvmField var ibSaveImage: ImageButton? = null
    @OnClick(R.id.ib_save_form)
    fun saveForm(){
        formNewPresenter.setForm(loadPack())
        formNewPresenter.registerForm()
    }
    @OnClick(R.id.ib_save_image)
    fun shootCamera(){
        manageImages.startCamera()
    }

    @OnClick(R.id.et_day)
    fun defineDate(){
        val newCalendar: Calendar = Calendar.getInstance()
        val startTime = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener {
                    _, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    etDay!!.setText(dateFormatter.format(newDate.time))
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
        startTime.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_new_form)
        component.inject(this)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        idFormSelect = intent.getStringExtra(ID_FORM)

    }

    private fun setupRecyclerView(){
        rvImages!!.setHasFixedSize(true)
        rvImages!!.layoutManager = GridLayoutManager(this, 2)
        //LinearLayoutManager(this)
        adapter = ImageAdapter()
        rvImages!!.adapter = adapter
    }
    override fun onStart() {
        super.onStart()
        tvTitle!!.text = resources.getString(R.string.lbl_new_form)
        etDay!!.inputType = InputType.TYPE_NULL
        etDay!!.setText(dateFormatter.format(Date()))
        image = ImageView(this)
        projectPresenter.view = this
        formNewPresenter.view = this
        addFormProjectListPresenter.view = this
        formRegisterNetworkPresenter.view = this
        addImageListPresenter.view = this
        formSelectPresenter.view = this
        imageListPresenter.view = this
        projectPresenter.getListProject()
        enableMyLocation()
        locationUser.updateLocation()
        fillForm()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{
                        val project = listModel[position]
                        idProject = project.id
                        idNetProject = project.idNet

                        return@map resources.getString(
                                com.mobile.fodein.R.string.new_filter)
                    }
                }
                .subscribe { result -> toast(result)})

    }

    private fun fillForm(){
        if (idFormSelect.isNotEmpty()){
            ibSaveForm!!.isEnabled = false
            ibSaveImage!!.isEnabled = false
            formSelectPresenter.setForm(idFormSelect)
            formSelectPresenter.getSelectForm()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_item_info){

        }
        if (id == R.id.action_item_help){

        }
        if (id == ACTION_HOME){
            this.navigate<MainListActivity>()
            this.finish()
        }

        return super.onOptionsItemSelected(item)

    }

    private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(this, T::class.java)
        startActivity(intent)
    }

    private fun loadPack():MutableMap<String, Any>{
        pack[Constants.FORM_ID] = ""
        pack[Constants.FORM_DATE] = etDay!!.text.toString()
        pack[Constants.FORM_LATITUDE] = locationUser.latitude
        pack[Constants.FORM_LONGITUDE] = locationUser.longitude
        pack[Constants.FORM_DATE_UPDATE] = etDay!!.text.toString()
        pack[Constants.FORM_ANNOTATION] = etAnnotation!!.text.toString()
        pack[Constants.FORM_ANNOTATION_ONE] = etAnnotationOne!!.text.toString()
        pack[Constants.FORM_ANNOTATION_TWO] = etAnnotationTwo!!.text.toString()
        pack[Constants.FORM_USER] = DeliveryOfResource.userId
        pack[Constants.FORM_PROJECT] = idProject
        pack[Constants.FORM_PROJECT_NET] = idNetProject

        return pack
    }
    private fun enableMyLocation() {
        when {
            ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ->
                permissionUtils.requestPermission(this,
                    LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun handleBackPressInThisActivity(){
        if (isWarnedToClose){
            CachingLruRepository.instance.getLru().evictAll()
            finish()
        }else{
            isWarnedToClose = true
            toast(getString(R.string.lbl_close_app))
            launch{
                delay(2000)
                isWarnedToClose = false
            }
        }
    }

    override fun onBackPressed() {
        handleBackPressInThisActivity()
    }

    //navigator.addFragment(R.id.fragmentContainer, UserListFragment())

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == manageImages.GALLERY_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK && data != null) {
            manageImages.setControlImage(data.data, image!!)
        } else if (requestCode == manageImages.CAMERA_IMAGE_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            val photoUri = FileProvider.getUriForFile(this,
                    applicationContext.packageName +
                            ".provider", manageImages.getCameraFile())
            manageImages.setControlImage(photoUri, image!!)
            this.listImages.add(image!!)
            adapter!!.setObjectList(this.listImages)
            rvImages!!.scrollToPosition(0)
            launchBuildMapperImage(image!!)

        }
    }

    private fun launchBuildMapperImage(capture: ImageView){
        launch(CommonPool){

            val mapperImage = MapperImage()
            val img: Bitmap = (capture.drawable as BitmapDrawable).bitmap
            mapperImage.date = DeliveryOfResource.getDateTime()
            mapperImage.image = manageImages.base641EncodedImage(img)
            mapperImage.latitude = locationUser.latitude
            mapperImage.longitude = locationUser.longitude
            listMapperImage.add(mapperImage)
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
            LOCATION_PERMISSION_REQUEST_CODE ->
                if (permissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                    enableMyLocation()
                }else {toast(getString(R.string.access_not_allowed))}
        }

    }

    private fun actionOnItemSelectedListenerObservable(): Observable<Int> {
        return Observable.create({
            e: ObservableEmitter<Int>? ->
            spFilter!!.onItemSelectedListener = object:
                    AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent:
                                            AdapterView<*>?,
                                            view: View?,
                                            position: Int, id: Long) {
                    e!!.onNext(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    e!!.setCancellable { Cancellable{
                        spFilter!!.onItemSelectedListener = null
                    } }
                }
            }

        })
    }

    private fun setDataSpinner(list: List<ProjectModel>){
        val contentSpinner: MutableList<String> = ArrayList()
        list.mapTo(contentSpinner) { it.name }
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context(),
                android.R.layout.simple_list_item_1, contentSpinner)
        spFilter!!.adapter = spinnerAdapter
        spFilter!!.setSelection(0)
    }

    override fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(message: String) {
        toast(message)
    }

    override fun hideLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showRetry() {
        if (connectionNetwork.checkConnect()){
            pack[Constants.FORM_PROJECT] = idNetProject
            pack.remove(Constants.FORM_PROJECT_NET)
            formRegisterNetworkPresenter.setForm(pack, DeliveryOfResource.token)
            formRegisterNetworkPresenter.registerForm()
        }
        projectPresenter.getListProject(true)
    }

    override fun hideRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        toast(message)
    }

    override fun context(): Context = this.applicationContext

    override fun <T> renderList(objectList: List<T>) {
        if (!objectList.isEmpty()){
            if (listModel.isEmpty()){
                listModel = objectList.filterIsInstance<ProjectModel>()
                if (listModel.isNotEmpty()){
                    setDataSpinner(listModel)
                }

            }else{
                val listImage = objectList.filterIsInstance<ImageModel>()
                if (listImage.isNotEmpty()){
                    setListImages(listImage)
                }
            }
        }

    }

    private fun setListImages(list: List<ImageModel>){
        for (i in list.indices){
            val image = list[i].image
            val bitmap = manageImages.base64DecodeImage(image)
            val imageView = ImageView(this)
            imageView.setImageBitmap(bitmap)
            this.listImages.add(imageView)
        }
        adapter!!.setObjectList(this.listImages)
        rvImages!!.scrollToPosition(0)
    }

    override fun <T> renderObject(obj: T) {
        if (obj != null){
            val idForm = (obj as FormModel).id
            if (idFormSelect.isEmpty()){
                pack[Constants.FORM_ID] = idForm
                toast((obj as FormModel).id)
                completeSaveDataForm(idForm)
            }else{
                setDataFormSelect((obj as FormModel))
                imageListPresenter.setVariables(idForm)
                imageListPresenter.getListImages(idForm)
            }
        }
    }

    private fun setDataFormSelect(formModel: FormModel){
        etDay!!.setText(formModel.date)
        etAnnotation!!.setText(formModel.annotation)
        etAnnotationOne!!.setText(formModel.annotationOne)
        etAnnotationTwo!!.setText(formModel.annotationTwo)
        searchProjectList(formModel.project_id)

    }

    private fun searchProjectList(id: String){
        listModel.indices
                .filter { listModel[it].idNet == id }
                .forEach { spFilter!!.setSelection(it) }
    }

    private fun addImagesOfForm(idForm: String){
        if (listMapperImage.isNotEmpty()){
            listMapperImage.indices.forEach { i ->
                listMapperImage[i].idForm = idForm }
            addImageListPresenter.setVariables(listMapperImage)
            addImageListPresenter.addImages()
        }

    }
    private fun completeSaveDataForm(idForm: String){
        launch(CommonPool){
            addFormProjectListPresenter.setVariables(idForm, idProject)
            addFormProjectListPresenter.addFormListProject()
            addImagesOfForm(idForm)
        }
    }

}




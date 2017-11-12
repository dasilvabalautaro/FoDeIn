package com.mobile.fodein.presentation.view.fragment

import android.R
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import com.mobile.fodein.App
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.IEntity
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.FormModel
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.presenter.FormPresenter
import com.mobile.fodein.presentation.presenter.ProjectPresenter
import com.mobile.fodein.presentation.view.component.ItemAdapter
import com.mobile.fodein.tools.ConnectionNetwork
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class FormFragment: BaseFragment(), ILoadDataView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var projectPresenter: ProjectPresenter
    @Inject
    lateinit var formPresenter: FormPresenter
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork

    private var listModel: List<ProjectModel> = ArrayList()

    init {
        val down = observableDown.map { d -> d }
        disposable.add(down.observeOn(Schedulers.newThread())
                .subscribe { d ->
                    kotlin.run {
                        if (d == 2){
                            projectPresenter.getListProject()
                        }
                    }
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(com.mobile.fodein.R.layout.view_list_data,
                container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        tvSearch!!.text = resources.getString(com.mobile.fodein.R.string.lbl_list_forms)
        ibNewForm!!.visibility = View.VISIBLE
        projectPresenter.view = this
        if (!connectionNetwork.isOnline()){
            projectPresenter.getListProject()
        }

    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{

                        val listFormsProject = listModel[position].list
                        showDataList(listFormsProject
                                , position)
                        return@map resources.getString(com.mobile.fodein.R.string.new_filter)
                    }
                }
                .subscribe { result -> context.toast(result)})
    }


    override fun showLoading() {
        srData!!.isRefreshing = true
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun hideLoading() {
        srData!!.isRefreshing = false
    }

    override fun showRetry() {
        refreshData()
    }

    override fun hideRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun context(): Context {
        return activity.applicationContext
    }

    override fun <T> renderList(objectList: List<T>) {
        if (!objectList.isEmpty()){
            listModel = objectList.filterIsInstance<ProjectModel>()
            setDataSpinner(listModel)
        }

    }

    override fun <T> renderObject(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setupRecyclerView(){
        rvData!!.setHasFixedSize(true)
        rvData!!.layoutManager = LinearLayoutManager(this.context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addDecorationRecycler()
        }
        adapter = ItemAdapter{
            if (!srData!!.isRefreshing) {
                //presenter.clickWeighing(it)
            }
        }

        rvData!!.adapter = adapter
    }

    private fun setupSwipeRefresh() = srData!!.setOnRefreshListener(
            this::refreshData)

//    projectPresenter::getListProject
    private fun refreshData(){
        val list = CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_PROJECT_MODEL)
        if (list != null && list is ArrayList<*> && list.size != 0){
            listModel = list.filterIsInstance<ProjectModel>()
            setDataSpinner(listModel)
        }else{
            projectPresenter.getListProject()
        }

        srData!!.isRefreshing = false
    }

    private fun selectIndexFilter(idSelect: String,
                                  list: List<ProjectModel>){
        for (i in list.indices){
            val project = list[i]
            if (project.id == idSelect){
                spFilter!!.setSelection(i)
                return
            }
        }
        spFilter!!.setSelection(0)
    }

    private fun setDataSpinner(list: List<ProjectModel>){
        val contentSpinner: MutableList<String> = ArrayList()
        list.mapTo(contentSpinner) { it.name }
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context(),
                R.layout.simple_list_item_1, contentSpinner)
        spFilter!!.adapter = spinnerAdapter
        selectIndexFilter(idSelect, list)
    }

    private fun showDataList(unityList: ArrayList<FormModel>, position: Int) {
        val iEntityList: ArrayList<IEntity> = ArrayList()
        unityList.mapTo(iEntityList) { it }
        adapter!!.setObjectList(iEntityList)
        rvData!!.scrollToPosition(position)
    }
}
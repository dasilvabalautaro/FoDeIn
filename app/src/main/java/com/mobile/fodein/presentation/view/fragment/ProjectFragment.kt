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
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.IEntity
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.ProjectModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.presentation.presenter.ProjectNetworkPresenter
import com.mobile.fodein.presentation.presenter.UnityPresenter
import com.mobile.fodein.presentation.view.component.ItemAdapter
import com.mobile.fodein.tools.ConnectionNetwork
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ProjectFragment: BaseFragment(), ILoadDataView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var unityPresenter: UnityPresenter
    @Inject
    lateinit var projectNetworkPresenter: ProjectNetworkPresenter
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork

    var listModel: List<UnityModel> = ArrayList()

    init {
        val down = observableDown.map { d -> d }
        disposable.add(down.observeOn(Schedulers.newThread())
                .subscribe { d ->
                    kotlin.run {
                        if (d == 1){
                            if (connectionNetwork.isOnline() &&
                                    !DeliveryOfResource.updateProjects){
                                projectNetworkPresenter.setVariables(DeliveryOfResource.token)
                                projectNetworkPresenter.getList()
                            }
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
        tvSearch!!.text = resources.getString(com.mobile.fodein.R.string.lbl_list_projects)
        unityPresenter.view = this
        projectNetworkPresenter.view = this
        if (!connectionNetwork.isOnline()){
            unityPresenter.getListUnity()
        }
    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{
                        idSelect = ""
                        val listProjectsUnity = listModel[position].list
                        showDataList(listProjectsUnity
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
        serviceDown = 2
        observableDown.onNext(serviceDown)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun context(): Context {
        return activity.applicationContext
    }

    override fun <T> renderList(objectList: List<T>) {
        if (!objectList.isEmpty()){
            listModel = objectList.filterIsInstance<UnityModel>()
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
                idSelect = it.id
                if (callbackSelect != null){
                    callbackSelect!!.select(this)
                }
            }
        }

        rvData!!.adapter = adapter
    }

    private fun setupSwipeRefresh() = srData!!.setOnRefreshListener(
            this::refreshData)

    private fun refreshData(){
        val list = CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_UNITY_MODEL)
        if (list != null && list is ArrayList<*> && list.size != 0){
            listModel = list.filterIsInstance<UnityModel>()
            setDataSpinner(listModel)
        }else{
            unityPresenter.getListUnity()
        }
        srData!!.isRefreshing = false
    }

    private fun selectIndexFilter(idSelect: String,
                                  list: List<UnityModel>){
        for (i in list.indices){
            val unity = list[i]
            if (unity.id == idSelect){
                spFilter!!.setSelection(i)
                return
            }
        }
        spFilter!!.setSelection(0)
    }

    private fun setDataSpinner(list: List<UnityModel>){
        val contentSpinner: MutableList<String> = ArrayList()
        list.mapTo(contentSpinner) { it.name }
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context(),
                R.layout.simple_list_item_1, contentSpinner)
        spFilter!!.adapter = spinnerAdapter
        selectIndexFilter(idSelect, list)

    }

    private fun showDataList(unityList: ArrayList<ProjectModel>, position: Int) {
        val iEntityList: ArrayList<IEntity> = ArrayList()
        unityList.mapTo(iEntityList) { it }
        adapter!!.setObjectList(iEntityList)
        rvData!!.scrollToPosition(position)
    }
}
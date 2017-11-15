package com.mobile.fodein.presentation.view.fragment

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
import com.mobile.fodein.R
import com.mobile.fodein.dagger.PresentationModule
import com.mobile.fodein.domain.DeliveryOfResource
import com.mobile.fodein.models.persistent.repository.CachingLruRepository
import com.mobile.fodein.presentation.interfaces.IEntity
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.presentation.presenter.DistrictNetworkPresenter
import com.mobile.fodein.presentation.presenter.DistrictPresenter
import com.mobile.fodein.presentation.presenter.UnityNetworkPresenter
import com.mobile.fodein.presentation.view.component.ItemAdapter
import com.mobile.fodein.tools.ConnectionNetwork
import com.mobile.fodein.tools.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


class UnityFragment : BaseFragment(), ILoadDataView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var districtPresenter: DistrictPresenter
    @Inject
    lateinit var districtNetworkPresenter: DistrictNetworkPresenter
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork
    @Inject
    lateinit var unityNetworkPresenter: UnityNetworkPresenter

    var listModel: List<DistrictModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.view_list_data,
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
        tvSearch!!.text = resources.getString(R.string.lbl_list_units)
        ibNewForm!!.visibility = View.INVISIBLE
        ibMap!!.visibility = View.INVISIBLE
        districtPresenter.view = this
        districtNetworkPresenter.view = this
        unityNetworkPresenter.view = this

        if (connectionNetwork.checkConnect() &&
                !DeliveryOfResource.updateDistrict){
            districtNetworkPresenter.setVariables(DeliveryOfResource.token)
            districtNetworkPresenter.getList()

        }else{
            districtPresenter.getListDistrict()
        }
    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map { position ->
                    run{
                        idSelect = ""
                        val listUnityDistrict = listModel[position].list
                        showDataList(listUnityDistrict
                                , position)
                        return@map resources.getString(R.string.new_filter)
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
        serviceDown = 1
        observableDown.onNext(serviceDown)
        refreshData()
    }

    override fun hideRetry() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun context(): Context = activity.applicationContext

    override fun <T> renderList(objectList: List<T>) {
        if (!objectList.isEmpty()){
            if (connectionNetwork.checkConnect() &&
                    !DeliveryOfResource.updateDistrict){
                unityNetworkPresenter.setVariables(DeliveryOfResource.token)
                unityNetworkPresenter.getList()
            }

            listModel = objectList.filterIsInstance<DistrictModel>()
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

    private fun setupSwipeRefresh() = srData!!
            .setOnRefreshListener(this::refreshData)

//    districtPresenter::getListDistrict
    private fun refreshData(){
        val list = CachingLruRepository
                .instance
                .getLru()
                .get(Constants.CACHE_LIST_DISTRICT_MODEL)
        if (list != null && list is ArrayList<*> && list.size != 0){
            listModel = list.filterIsInstance<DistrictModel>()
            setDataSpinner(listModel)
        }else{
            districtPresenter.getListDistrict()
        }
        srData!!.isRefreshing = false
    }

    private fun setDataSpinner(list: List<DistrictModel>){
        val contentSpinner: MutableList<String> = ArrayList()
        list.mapTo(contentSpinner) { it.name }
        val spinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context(),
                android.R.layout.simple_list_item_1, contentSpinner)
        spFilter!!.adapter = spinnerAdapter
        spFilter!!.setSelection(0)
    }

    private fun showDataList(unityList: ArrayList<UnityModel>, position: Int) {
        val iEntityList: ArrayList<IEntity> = ArrayList()
        unityList.mapTo(iEntityList) { it }
        adapter!!.setObjectList(iEntityList)
        rvData!!.scrollToPosition(position)
    }

}
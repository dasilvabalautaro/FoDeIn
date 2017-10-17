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
import com.mobile.fodein.presentation.interfaces.IEntity
import com.mobile.fodein.presentation.interfaces.ILoadDataView
import com.mobile.fodein.presentation.model.DistrictModel
import com.mobile.fodein.presentation.model.UnityModel
import com.mobile.fodein.presentation.presenter.DistrictPresenter
import com.mobile.fodein.presentation.view.component.ItemAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UnityFragment : BaseFragment(), ILoadDataView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var districtPresenter: DistrictPresenter


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
        districtPresenter.view = this
        districtPresenter.getListDistrict()

    }

    override fun onStart() {
        super.onStart()
        disposable.add( actionOnItemSelectedListenerObservable()
                .observeOn(Schedulers.newThread())
                .map { position ->
                    run{
                        val listUnityDistrict = listModel[position].list
                        showDataList(listUnityDistrict as ArrayList<UnityModel>
                                , position)
                        return@map resources.getString(R.string.new_filter)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                //presenter.clickWeighing(it)
            }
        }

        rvData!!.adapter = adapter
    }

    private fun setupSwipeRefresh() = srData!!.setOnRefreshListener(
            districtPresenter::getListDistrict)

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
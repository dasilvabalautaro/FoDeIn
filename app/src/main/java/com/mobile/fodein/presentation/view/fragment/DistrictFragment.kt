package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import butterknife.BindView
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
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Cancellable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DistrictFragment: BaseFragment(), ILoadDataView {
    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresentationModule(context))}

    @Inject
    lateinit var districtPresenter: DistrictPresenter

    var adapter: ItemAdapter? = null
    var listModel: List<DistrictModel> = ArrayList()

    @BindView(R.id.sp_filter)
    @JvmField var spFilter: Spinner? = null
    @BindView(R.id.sr_data)
    @JvmField var srData: SwipeRefreshLayout? = null
    @BindView(R.id.rv_data)
    @JvmField var rvData: RecyclerView? = null

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


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addDecorationRecycler(){
        val horizontalDecoration =
                DividerItemDecoration(rvData!!.context,
                        DividerItemDecoration.VERTICAL)
        val horizontalDivider: Drawable = context
                .getDrawable(R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider)
        rvData!!.addItemDecoration(horizontalDecoration)
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

    private fun showDataList(unityList: ArrayList<UnityModel>, position: Int) {
        val iEntityList: ArrayList<IEntity> = ArrayList()
        unityList.mapTo(iEntityList) { it }
        adapter!!.setObjectList(iEntityList)
        rvData!!.scrollToPosition(position)
    }

}
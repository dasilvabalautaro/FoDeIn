package com.mobile.fodein.presentation.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.OnClick
import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.activities.MainActivity
import com.mobile.fodein.presentation.view.component.ItemAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Cancellable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


abstract class BaseFragment: Fragment() {

    companion object Factory{
        var serviceDown = 0
        var observableDown: Subject<Int> = PublishSubject.create()
        var idSelect: String = ""
    }

    interface CallbackSelect{
        fun select(fragment: BaseFragment)
    }

    protected var disposable: CompositeDisposable = CompositeDisposable()
    var adapter: ItemAdapter? = null
    var callbackSelect: CallbackSelect? = null

    @BindView(R.id.sp_filter)
    @JvmField var spFilter: Spinner? = null
    @BindView(R.id.sr_data)
    @JvmField var srData: SwipeRefreshLayout? = null
    @BindView(R.id.rv_data)
    @JvmField var rvData: RecyclerView? = null
    @BindView(R.id.tv_search)
    @JvmField var tvSearch: TextView? = null
    @BindView(R.id.ib_new_form)
    @JvmField var ibNewForm: ImageButton? = null
    @OnClick(R.id.ib_new_form)
    fun executeNewForm(){
        activity.navigate<MainActivity>()
        activity.finish()
    }
    init {
        observableDown
                .subscribe { serviceDown }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    protected fun addDecorationRecycler(){
        val horizontalDecoration =
                DividerItemDecoration(rvData!!.context,
                        DividerItemDecoration.VERTICAL)
        val horizontalDivider: Drawable = context
                .getDrawable(R.drawable.horizontal_divider)
        horizontalDecoration.setDrawable(horizontalDivider)
        rvData!!.addItemDecoration(horizontalDecoration)
    }

    protected fun actionOnItemSelectedListenerObservable(): Observable<Int> {
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

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private inline fun <reified T : Activity> Activity.navigate() {
        val intent = Intent(activity, T::class.java)
        startActivity(intent)
    }
}
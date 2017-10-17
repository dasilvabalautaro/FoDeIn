package com.mobile.fodein.presentation.view.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import com.mobile.fodein.R
import com.mobile.fodein.presentation.view.component.ItemAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Cancellable


abstract class BaseFragment: Fragment() {
    protected var disposable: CompositeDisposable = CompositeDisposable()
    var adapter: ItemAdapter? = null
    @BindView(R.id.sp_filter)
    @JvmField var spFilter: Spinner? = null
    @BindView(R.id.sr_data)
    @JvmField var srData: SwipeRefreshLayout? = null
    @BindView(R.id.rv_data)
    @JvmField var rvData: RecyclerView? = null


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

}
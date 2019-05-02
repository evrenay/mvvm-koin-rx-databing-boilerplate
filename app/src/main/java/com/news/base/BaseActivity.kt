package com.news.base

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseActivity<VM : BaseViewModel,DB: ViewDataBinding> : BaseView, AppCompatActivity() {

    protected lateinit var viewModel: VM

    //lateinit var progressAlertDialog : ProgressAlertDialog

    lateinit var binding: DB

    var refreshLayout : SwipeRefreshLayout?=null

    protected abstract fun getViewModel(): Class<VM>

    override fun showLoading() {
        //progressAlertDialog.showDialog()
    }

    override fun hideLoading() {
        //progressAlertDialog.cancelDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, getLayoutRes())

        //progressAlertDialog = ProgressAlertDialog(this)

      //  viewModel = ViewModelProviders.of(this).get(getViewModel())


        initializeRefreshLayout()
        showHideLoading()
        showError()

    }



    override fun getContext(): Context {
        return this
    }

    @LayoutRes
    protected abstract fun getLayoutRes() : Int

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(base!!));
    }


    open fun initializeRefreshLayout(){
        viewModel.swipeLoadingStatus.observe(this, Observer {
            status->
            if(refreshLayout!=null){
                if(status!!){
                    refreshLayout!!.post({ refreshLayout!!.setRefreshing(true) })
                }
                else{
                    refreshLayout!!.post({ refreshLayout!!.setRefreshing(false) })
                }
            }

        })
    }

    open fun showError(){
        viewModel.errorString.observe(this, Observer {
            errString->
           // InfoAlertDialog(this,errString!!,{}).showDialog()
        })
    }

    open fun showHideLoading(){
        viewModel.loadingStatus.observe(this, Observer {
            status->
            if(status!!){
                showLoading()
            }
            else{
                hideLoading()
            }
        })
    }

    open fun showNoItemView(status : Boolean){

    }
}
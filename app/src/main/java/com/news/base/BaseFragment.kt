package com.news.base

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by a on 4.04.2018.
 */
abstract class BaseFragment<VM : BaseViewModel,DB:ViewDataBinding>() :  Fragment()  {




    protected lateinit var viewModel: VM

    //lateinit var progressAlertDialog : ProgressAlertDialog


    lateinit var binding: DB


    var refreshLayout : SwipeRefreshLayout?=null

    fun showLoading() {
        //progressAlertDialog.showDialog()
    }

    fun hideLoading() {

        //progressAlertDialog.cancelDialog()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(getViewModel())
        //progressAlertDialog = ProgressAlertDialog(this.activity!! as AppCompatActivity)

        initializeRefreshLayout()
        showHideLoading()
        showError()
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.getRoot()
    }



    @LayoutRes
    protected abstract fun getLayoutRes() : Int


    protected abstract fun getViewModel(): Class<VM>

    open fun showError(){
        viewModel.errorString.observe(this, Observer {
            errString->
            //InfoAlertDialog(activity as AppCompatActivity,errString!!,{}).showDialog()


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


    open fun refresh(){

    }



    open fun showNoItemView(status : Boolean){

    }
}
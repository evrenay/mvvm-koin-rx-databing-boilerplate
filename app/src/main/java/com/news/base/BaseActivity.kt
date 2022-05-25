package com.news.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseView,
    AppCompatActivity() {

    protected lateinit var viewModel: VM

    //lateinit var progressAlertDialog : ProgressAlertDialog

    lateinit var binding: DB

    var refreshLayout: SwipeRefreshLayout? = null

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
        initializeRefreshLayout()
        showHideLoading()
        showError()
    }


    override fun getContext(): Context {
        return this
    }

    @LayoutRes
    protected abstract fun getLayoutRes(): Int


    open fun initializeRefreshLayout() {
        viewModel.swipeLoadingStatus.observe(this) { status ->
            if (refreshLayout != null) {
                if (status!!) {
                    refreshLayout!!.post { refreshLayout!!.isRefreshing = true }
                } else {
                    refreshLayout!!.post { refreshLayout!!.isRefreshing = false }
                }
            }
        }
    }

    open fun showError() {
        viewModel.errorString.observe(this) {
            // InfoAlertDialog(this,errString!!,{}).showDialog()
        }
    }

    open fun showHideLoading() {
        viewModel.loadingStatus.observe(this) { status ->
            if (status!!) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    open fun showNoItemView(status: Boolean) {}
}
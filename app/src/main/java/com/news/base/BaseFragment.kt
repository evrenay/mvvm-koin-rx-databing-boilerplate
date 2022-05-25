package com.news.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by a on 4.04.2018.
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding>() : Fragment() {
    protected lateinit var viewModel: VM

    //lateinit var progressAlertDialog : ProgressAlertDialog

    lateinit var binding: DB

    var refreshLayout: SwipeRefreshLayout? = null

    private fun showLoading() {
        //progressAlertDialog.showDialog()
    }

    private fun hideLoading() {

        //progressAlertDialog.cancelDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(getViewModel())
        //progressAlertDialog = ProgressAlertDialog(this.activity!! as AppCompatActivity)

        initializeRefreshLayout()
        showHideLoading()
        showError()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root
    }


    @LayoutRes
    protected abstract fun getLayoutRes(): Int


    protected abstract fun getViewModel(): Class<VM>

    open fun showError() {
        viewModel.errorString.observe(this, Observer { errString ->
            //InfoAlertDialog(activity as AppCompatActivity,errString!!,{}).showDialog()

        })
    }

    open fun showHideLoading() {
        viewModel.loadingStatus.observe(this, Observer { status ->
            if (status!!) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }


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

    open fun refresh() {}

    open fun showNoItemView(status: Boolean) {}
}
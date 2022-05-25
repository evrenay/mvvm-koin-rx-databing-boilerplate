package com.news.base

import android.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup

abstract class BaseDialog<VM : BaseViewModel, DB : ViewDataBinding>(val activity: AppCompatActivity) {

    var alertDialog: AlertDialog
        protected set

    private var dialogView: View? = null

    var refreshLayout: SwipeRefreshLayout? = null

    var viewModel: VM

    var binding: DB

    fun findViewById(id: Int): View? {
        return if (dialogView != null) {
            dialogView!!.findViewById(id)
        } else null
    }

    fun cancelDialog() {
        alertDialog.cancel()
    }

    fun showDialog() {
        alertDialog.show()
    }

    protected abstract fun setupDialog()

    protected abstract fun instantiateViewModel(): VM

    @LayoutRes
    protected abstract fun getLayoutRes(): Int

    init {
        val dialogBuilder = onCreateAlertDialogBuilder()
        val inflater = activity.layoutInflater
        viewModel = instantiateViewModel()
        binding = DataBindingUtil.inflate(
            inflater,
            getLayoutRes(),
            activity.findViewById(android.R.id.content) as ViewGroup,
            false
        )
        dialogView = binding.root
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create()

        showError()
        initializeRefreshLayout()
    }


    private fun onCreateAlertDialogBuilder(): AlertDialog.Builder {
        return AlertDialog.Builder(activity)
    }


    fun showError() {
        viewModel.errorString.observe(activity, Observer { errString ->
            //InfoAlertDialog(activity,errString!!,{}).showDialog()


        })
    }


    private fun initializeRefreshLayout() {
        viewModel.swipeLoadingStatus.observe(activity) { status ->
            if (refreshLayout != null) {
                if (status!!) {
                    refreshLayout!!.post { refreshLayout!!.isRefreshing = true }
                } else {
                    refreshLayout!!.post { refreshLayout!!.isRefreshing = false }
                }
            }

        }
    }

    open fun showNoItemView(status: Boolean) {}

}

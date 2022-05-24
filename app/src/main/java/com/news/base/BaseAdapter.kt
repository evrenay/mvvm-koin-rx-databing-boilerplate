package com.news.base

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class BaseAdapter<T : BaseModel>(

    private var entityList: List<T>,
    internal var context: Context,
    private var viewId: Int,
    private var dataId: Int,
    internal var viewModel: BaseViewModel?,
    private var viewModelId: Int,
    private var onItemValidate: ((binding: ViewDataBinding, data: T, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewId,
            parent,
            false
        )
        return BaseViewHolder<T>(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bindTo(
            entityList[position],
            dataId,
            viewModelId,
            viewModel,
            position,
            onItemValidate
        )
    }


    override fun getItemCount(): Int {
        return entityList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun update(entityList: List<T>) {
        this.entityList = entityList
        notifyDataSetChanged()
    }

    fun getList(): List<T> {
        return entityList
    }

    class BaseViewHolder<T : BaseModel>(internal var binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(
            data: T?,
            dataId: Int,
            viewModelId: Int,
            viewModel: BaseViewModel?,
            position: Int,
            onItemValidate: ((binding: ViewDataBinding, data: T, position: Int) -> Unit)? = null
        ) {
            if (onItemValidate != null && data != null) onItemValidate(binding, data, position)
            if (data != null) binding.setVariable(dataId, data)
            if (viewModel != null) binding.setVariable(viewModelId, viewModel)
            binding.executePendingBindings()

        }
    }

}
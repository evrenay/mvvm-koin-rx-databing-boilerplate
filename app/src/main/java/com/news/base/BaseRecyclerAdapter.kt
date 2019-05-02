package com.news.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.news.BR


abstract class BaseRecyclerAdapter<T : BaseModel, VM : BaseRecyclerItemViewModel<T>,DB: ViewDataBinding>(val activity:AppCompatActivity) : RecyclerView.Adapter<BaseRecyclerAdapter.ItemViewHolder<T, VM,DB>>() {

    var items : List<T>


    protected abstract fun onItemBinding(binding:DB,viewModel : VM,position:Int)

    protected abstract fun getViewModel() : VM

    @LayoutRes
    protected abstract fun getLayoutRes() : Int

    init {
        items = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<T, VM, DB> {
        val binding  = DataBindingUtil.inflate<DB>(LayoutInflater.from(parent.context), getLayoutRes(), parent, false)

        val viewModel = getViewModel()

        return ItemViewHolder(binding.root,binding,viewModel) {
            binding, viewModel, position ->
            onItemBinding(binding,viewModel,position)
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder<T, VM, DB>, position: Int) {
        holder.setItem(items.get(position),position)

    }


    fun clearItems(){
        (items as MutableList).clear()
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun update(items:List<T>) {
        this.items = items
        notifyDataSetChanged()
    }


    fun removeItem(position:Int){
        this.notifyItemRemoved(position);
    }


    fun setItem(items:List<T>){
        this.items = items
    }


    fun update(){
        notifyDataSetChanged()
    }



    class ItemViewHolder<T:BaseModel, VM : BaseRecyclerItemViewModel<T>,DB:ViewDataBinding>(
            itemView: View,
            val binding: DB,
            val viewModel: VM,
            val onItemBinding : (binding:DB,viewModel : VM,position:Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        internal fun setItem(item: T,position: Int) {
            viewModel.model = item
            viewModel.clearCache()
            viewModel.setValues()
            binding.setVariable(BR.viewModel, viewModel) // layout'taki viewModel variable'ın adı "viewModel" olmalı
            onItemBinding(binding,viewModel,position)
            binding.executePendingBindings()
        }
    }
}
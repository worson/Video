package app.sen.video.view.rvhelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import app.sen.video.view.rvhelper.ExtListAdapter

open class DataBindingListAdapter<T, DB : ViewDataBinding>(
    private val layoutId: Int,
    private val variableId: Int,
    diffCallback: DiffUtil.ItemCallback<T>,
    private val onBind: ((DB, T, Int, List<Any>) -> Unit)? = null
) :
    ExtListAdapter<T, DataBindingViewHolder<DB>>(diffCallback) {


    override fun onCreateCustomViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<DB> {
        return DataBindingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }

    override fun onBindCustomViewHolder(
        holder: DataBindingViewHolder<DB>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        val binding = holder.getBinding()

        if (0 != variableId) {
            binding.setVariable(variableId, item)
        }

        onBind?.invoke(
            binding,
            item,
            convertRealDataPosition(position),
            payloads
        )

        binding.executePendingBindings()
    }
}


class DataBindingViewHolder<DB : ViewDataBinding>(itemView: View) :
    BaseViewHolder(itemView) {

    private lateinit var dataBinding: DB


    fun getBinding(): DB {
        if (!this::dataBinding.isInitialized) {
            dataBinding = DataBindingUtil.bind(itemView)!!
        }


        return dataBinding
    }

}


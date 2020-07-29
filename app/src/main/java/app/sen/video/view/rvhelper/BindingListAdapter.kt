package app.sen.video.view.rvhelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * created by canxionglian on 2019-11-10
 */
class BindingListAdapter<T>(diffCallback: DiffUtil.ItemCallback<T>) :
//    PagedListAdapter<T, CommViewHolder<T>>(diffCallback) {
    ListAdapter<T, BindingViewHolder>(diffCallback) {


    private lateinit var mHeaderLayout: FrameLayout
    private lateinit var mFooterLayout: FrameLayout


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ITEM_TYPE_HEADER
            itemCount - 1 -> ITEM_TYPE_FOOTER
            else -> _getItemViewType(convertRealDataPosition(position))
        }
    }


    private fun _getItemViewType(position: Int): Int {
        mItemTypes.forEach { (index, item) ->
            if (item.action(getItem(position))) {
                return index
            }
        }
        return 0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return when (viewType) {
            ITEM_TYPE_HEADER -> BindingViewHolder(getHeaderLayout(parent.context))
            ITEM_TYPE_FOOTER -> BindingViewHolder(getFooterLayout(parent.context))
            else -> {
                BindingViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        mItemTypes[viewType]!!.layoutId,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        if (position == 0 || position == itemCount - 1) {
            return
        }

        mItemTypes[_getItemViewType(convertRealDataPosition(position))]?.bind?.invoke(
            holder.getBinding(),
            getItem(convertRealDataPosition(position)),
//            convertRealDataPosition(position)
            position
        )
    }

    fun convertRealDataPosition(position: Int): Int {
        return position - 1
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + 2
    }


    fun getItemRealCount(): Int {
        return super.getItemCount()
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        super.registerAdapterDataObserver(AdapterDataObserverProxy(observer, 1))
    }


    private fun getHeaderLayout(context: Context): ViewGroup {
        if (!this::mHeaderLayout.isInitialized) {
            mHeaderLayout = FrameLayout(context)
            mHeaderLayout.minimumHeight = 1
            mHeaderLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return mHeaderLayout
    }

    private fun getFooterLayout(context: Context): ViewGroup {
        if (!this::mFooterLayout.isInitialized) {
            mFooterLayout = FrameLayout(context)
            mFooterLayout.minimumHeight = 1
            mFooterLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return mFooterLayout
    }

    fun addHeaderView(header: View) {
        getHeaderLayout(header.context).addView(header)
    }

    fun addFooterView(footer: View) {
        getFooterLayout(footer.context).addView(footer)
    }

    companion object {
        private const val ITEM_TYPE_HEADER = -1
        private const val ITEM_TYPE_FOOTER = -2
    }


    private val mItemTypes = mutableMapOf<Int, Item<T>>()


    fun addItemType(
        layoutId: Int,
        action: (T) -> Boolean,
        bind: (ViewDataBinding, T, Int) -> Unit
    ) {
        mItemTypes[mItemTypes.size] = Item(layoutId, action, bind)
    }

    data class Item<T>(
        val layoutId: Int,
        val action: (T) -> Boolean,
        val bind: (ViewDataBinding, T, Int) -> Unit
    )
}


class BindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var dataBinding: ViewDataBinding


    fun getBinding(): ViewDataBinding {
        if (!this::dataBinding.isInitialized) {
            dataBinding = DataBindingUtil.bind(itemView)!!
        }


        return dataBinding
    }

}


class AdapterDataObserverProxy(
    private val adapterDataObserver: RecyclerView.AdapterDataObserver,
    private val headerCount: Int
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        adapterDataObserver.onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        adapterDataObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeInserted(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        adapterDataObserver.onItemRangeRemoved(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount)
    }
}
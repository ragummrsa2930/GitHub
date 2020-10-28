package com.lcldost.partnerapp.adapter

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ragu.github.R
import com.ragu.github.data.model.GitHubModel
import com.ragu.github.databinding.ListInflateBinding


class AdapterItem(context: Activity, private val onLoadMoreListener: OnLoadMoreListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_ITEM = 1
    private val VIEW_PROG = 0
    private val prList: ArrayList<GitHubModel?> = ArrayList()
    private val visibleThreshold = 1
    private val context: Context
    private var firstVisibleItem: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var isMoreLoading = false

    init {
        this.context = context
    }

    fun setLinearLayoutManager(linearLayoutManager: LinearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager
    }

    fun setRecyclerView(mView: RecyclerView) {
        mView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.childCount
                totalItemCount = mLinearLayoutManager!!.itemCount
                firstVisibleItem = mLinearLayoutManager!!.findFirstVisibleItemPosition()
                if (!isMoreLoading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    onLoadMoreListener?.onLoadMore()
                    isMoreLoading = true
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        return if (prList[position] != null) VIEW_ITEM else VIEW_PROG
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vh: RecyclerView.ViewHolder
        if (viewType == VIEW_ITEM) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding: ListInflateBinding = ListInflateBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            vh = PRViewHolder(itemBinding)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.progressbar_item, parent, false
            )

            vh = ProgressViewHolder(v)
        }
        return vh
    }

    fun addAll(lst: ArrayList<GitHubModel>) {
        prList.clear()
        prList.addAll(lst)
        notifyDataSetChanged()
    }

    fun addItemMore(lst: ArrayList<GitHubModel>) {
        prList.addAll(lst)
        notifyItemRangeChanged(0, prList.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        if (holder is PRViewHolder) {
            val gitHubModel = prList[position]
            holder.bind(gitHubModel!!)
        } else {
            (holder as ProgressViewHolder).loadMoreProgressBar.isIndeterminate = true
        }

    fun setMoreLoading(isMoreLoading: Boolean) {
        this.isMoreLoading = isMoreLoading
    }

    override fun getItemCount(): Int {
        return prList.size
    }

    fun setProgressMore(isProgress: Boolean) {
        if (isProgress) {
            Handler().post {
                prList.add(null)
                notifyItemInserted(prList.size - 1)
            }
        } else {
            prList.removeAt(prList.size - 1)
            notifyItemRemoved(prList.size)
        }
    }


    interface OnLoadMoreListener {
        fun onLoadMore()
    }


    private class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val loadMoreProgressBar: ProgressBar = v.findViewById(R.id.progressBar1)

    }

    internal class PRViewHolder(private val binding: ListInflateBinding)  : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(gitHubModel: GitHubModel) {
            binding.gitmodel = gitHubModel
            binding.executePendingBindings()
        }
    }

    companion object {
        private val TAG = "AdapterItem"
    }
}

package com.qapital.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.qapital.BR
import com.qapital.R
import com.qapital.base.BaseFragment
import com.qapital.base.QapitalApplication
import com.qapital.base.vm.ItemViewModel
import com.qapital.base.vm.LoadingFooterItemViewModel
import com.qapital.bindings.tatarka.BindingRecyclerViewAdapterIds
import com.qapital.bindings.tatarka.QapitalBindingRecyclerViewAdapter
import com.qapital.databinding.FragmentActivitiesBinding
import com.qapital.ui.activities.vm.ActivityItemViewModel
import com.qapital.utils.ConsumableState
import com.qapital.utils.bindingProvider
import com.qapital.utils.paging.PagingScrollListener
import com.qapital.utils.viewModelProvider
import com.qapital.widgets.itemdecorators.IndicesSkippingDividerItemDecoration
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class ActivitiesFragment : BaseFragment<ActivitiesViewModel>(),
    QapitalBindingRecyclerViewAdapter.OnAdapterCountChangedListener {

    private companion object {
        private const val VISIBLE_THRESHOLD = 3
        private const val RECYCLER_VIEW_STATE = "recycler_view_state"
    }

    override val viewModel: ActivitiesViewModel by viewModelProvider(
        { viewModelFactory },
        true
    )

    private val binding: FragmentActivitiesBinding by bindingProvider(R.layout.fragment_activities)

    val itemBinding = OnItemBindClass<ItemViewModel>()
        .map(ActivityItemViewModel::class.java, BR.viewModel, R.layout.item_activity)
        .map(LoadingFooterItemViewModel::class.java, ItemBinding.VAR_NONE, R.layout.item_loading_footer)

    val itemIds = BindingRecyclerViewAdapterIds

    val adapter = QapitalBindingRecyclerViewAdapter<ItemViewModel>()
    private var restoredRecyclerViewState: Parcelable? = null

    lateinit var pagingScrollListener: PagingScrollListener

    val diffConfig = AsyncDifferConfig.Builder(object :
        DiffUtil.ItemCallback<ItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ItemViewModel,
            newItem: ItemViewModel
        ): Boolean {
            return if (oldItem::class == newItem::class) {
                oldItem.itemId() == newItem.itemId()
            } else {
                false
            }
        }

        override fun areContentsTheSame(
            oldItem: ItemViewModel,
            newItem: ItemViewModel
        ): Boolean {
            return if (oldItem is ActivityItemViewModel) {
                newItem as ActivityItemViewModel

                oldItem.avatarUrl == newItem.avatarUrl
            } else {
                true
            }
        }
    }).build()

    lateinit var itemDecoration: IndicesSkippingDividerItemDecoration

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as QapitalApplication).appComponent.activitiesBuilder()
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.toolbar.setupWithNavController(
            navController,
            AppBarConfiguration(navController.graph)
        )

        itemDecoration = IndicesSkippingDividerItemDecoration(
            requireContext(),
            IndicesSkippingDividerItemDecoration.VERTICAL,
            ContextCompat.getDrawable(requireContext(), R.drawable.divider_grey_100)!!
        )

        pagingScrollListener = PagingScrollListener(
            binding.recyclerView.layoutManager!!,
            VISIBLE_THRESHOLD, viewModel::onLoadNextPage
        )
        adapter.setOnAdapterCountChangedCallback(this)
        binding.fragment = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.paginationErrorData.observe(viewLifecycleOwner, ::onPaginationError)
    }

    private fun onPaginationError(paginationError: ConsumableState<Throwable>) {
        paginationError.consume {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }

    fun onErrorRetry(@Suppress("UNUSED_PARAMETER") view: View) {
        viewModel.onRefresh()
    }

    fun onRefresh() {
        viewModel.onRefresh(swipeRefreshing = true)
    }

    override fun onAdapterCountChanged(count: Int) {
        if (count > 0) {
            itemDecoration.setSkipIndices(setOf(count - 1))
            restoredRecyclerViewState?.also { recyclerViewState ->
                binding.recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(RECYCLER_VIEW_STATE,
            Bundle().apply {
                putParcelable(
                    RECYCLER_VIEW_STATE,
                    binding.recyclerView.layoutManager?.onSaveInstanceState()
                )
            }
        )
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        restoredRecyclerViewState = savedInstanceState?.getBundle(RECYCLER_VIEW_STATE)
            ?.getParcelable(RECYCLER_VIEW_STATE)
    }
}

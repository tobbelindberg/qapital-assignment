package com.qapital.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.qapital.base.vm.BaseViewModel
import com.qapital.di.GenericViewModelFactory
import com.qapital.utils.simpleName
import javax.inject.Inject


abstract class BaseDialogFragment<VM : BaseViewModel> : AppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<VM>

    protected abstract val viewModel: VM

    fun show(manager: FragmentManager) {
        super.show(manager, simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initStateObservable()
    }

}
package com.qapital.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.qapital.base.vm.BaseViewModel
import com.qapital.di.GenericViewModelFactory
import javax.inject.Inject


abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    val navController by lazy { findNavController() }

    @Inject
    lateinit var viewModelFactory: GenericViewModelFactory<VM>

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initStateObservable()
    }

}
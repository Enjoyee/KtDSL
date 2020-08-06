package com.glimmer.mvvm.delegate

import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelStoreOwner

internal interface ApplicationDelegate : ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
}
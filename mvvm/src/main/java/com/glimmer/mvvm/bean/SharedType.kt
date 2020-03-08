package com.glimmer.mvvm.bean

sealed class SharedType {
    class Toast() : SharedType()

    class LoadingDialog() : SharedType()
    class DismissLoadingDialog() : SharedType()

    class LoadingLayout() : SharedType()
    class ErrorLayout() : SharedType()
    class EmptyLayout() : SharedType()
    class ContentLayout() : SharedType()
}
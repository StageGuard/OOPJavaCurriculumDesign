package me.stageguard.oopcd.frontend.desktop.ui

import com.arkivanov.decompose.statekeeper.Parcelable

sealed class ChildrenStates : Parcelable {
    object FlashView : ChildrenStates()
    object RollView : ChildrenStates()
    object SettingView : ChildrenStates()
}

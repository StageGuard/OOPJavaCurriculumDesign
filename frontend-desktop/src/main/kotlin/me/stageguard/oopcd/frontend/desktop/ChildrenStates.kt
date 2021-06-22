package me.stageguard.oopcd.frontend.desktop

import com.arkivanov.decompose.statekeeper.Parcelable

sealed class ChildrenStates : Parcelable {
    object FlashView : ChildrenStates()
    object RollView : ChildrenStates()
}

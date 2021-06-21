package me.stageguard.oopcd.frontend.desktop

import com.arkivanov.decompose.statekeeper.Parcelable

sealed class ChildrenStates : Parcelable {
    object FlashView : ChildrenStates()
    class RollView(
        var isRolling: Boolean = false
    ) : ChildrenStates()
}

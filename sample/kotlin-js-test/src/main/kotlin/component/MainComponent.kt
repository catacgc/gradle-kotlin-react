package component

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.h1
import react.dom.p

interface AppState : RState, RProps {
    var left: Int
    var right: Int
    var isValidInput: Boolean
}

class MainComponent : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        left = 0
        right = 0
        isValidInput = true
    }

    override fun RBuilder.render() {
        h1 { +"A Sum In Kotlin React" }

        child(SumComponent::class) {
            attrs.updateLeft = ::updateLeft
            attrs.updateRight = ::updateRight
            attrs.isValidInput = state.isValidInput
        }

        p { +"${state.left} + ${state.right} = ${state.left + state.right}" }
    }

    private fun updateLeft(event: Event) {
        val value = (event.target as HTMLInputElement).value

        setState {
            left = value.toIntOrNull() ?: 0
            isValidInput = value.toIntOrNull() != null
        }
    }

    private fun updateRight(event: Event) {
        val value = (event.target as HTMLInputElement).value

        setState{
            right = value.toIntOrNull() ?: 0
            isValidInput = value.toIntOrNull() != null
        }
    }

}
import kotlinext.js.require.context
import kotlinext.js.requireAll
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.Document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*
import kotlin.browser.document

fun main() {
    requireAll(context("src", true, js("/\\.css$/")))

    document.ready {
        render(document.getElementById("root")) {
            child(MainComponent::class) {}
        }
    }
}

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

        setState({ state ->
            state.left = value.toIntOrNull() ?: 0
            state.isValidInput = value.toIntOrNull() != null
            state
        })
    }

    private fun updateRight(event: Event) {
        val value = (event.target as HTMLInputElement).value

        setState({ state ->
            state.right = value.toIntOrNull() ?: 0
            state.isValidInput = value.toIntOrNull() != null
            state
        })
    }

}

interface SumComponentProps : RProps {
    var updateLeft: (Event) -> Unit
    var updateRight: (Event) -> Unit
    var isValidInput: Boolean
}

class SumComponent(props: SumComponentProps) : RComponent<SumComponentProps, RState>(props) {

    override fun RBuilder.render() {
        form {
            label { +"Left" }
            input(type = InputType.text) {
                attrs.onChangeFunction = props.updateLeft
            }

            label { +"Right" }
            input(type = InputType.text) {
                attrs.onChangeFunction = props.updateRight
            }

            if (!props.isValidInput) {
                p(classes = "error") {
                    + "Invalid number"
                }
            }
        }
    }
}

fun Document.ready(handler: () -> Unit) {
    document.addEventListener("DOMContentLoaded", { handler() })
}
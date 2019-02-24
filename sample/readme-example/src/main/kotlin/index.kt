import kotlinx.html.js.onClickFunction
import org.w3c.dom.Element
import react.*
import react.dom.*
import kotlin.browser.document

fun main() {
    document.onreadystatechange = {
        document.getElementById("root")?.let { startReact(it) }
    }
}

private fun startReact(elementById: Element) {
    render(elementById) {
        child(SimpleComponent::class) {
            attrs.startFrom = 10
        }
    }
}

interface SimpleProps : RProps {
    var startFrom: Int
}

interface SimpleState : RState {
    var currentValue: Int
}

class SimpleComponent(props: SimpleProps) : RComponent<SimpleProps, SimpleState>(props) {

    override fun SimpleState.init(props: SimpleProps) {
        currentValue = props.startFrom
    }

    override fun RBuilder.render() {
        h3 { +"Hello React" }

        div {
            +"This is a basic example. For more complex examples see "
            a(href = "https://github.com/JetBrains/kotlin-wrappers/tree/master/kotlin-react") {
                +"the Kotlin React Docs"
            }
        }

        div {
            h4 { +"Current counter value = ${state.currentValue}" }

            button {
                +"Increment"
                attrs.onClickFunction = { setState { currentValue++ } }
            }

            button {
                +"Decrement"
                attrs.onClickFunction = { setState { currentValue-- } }
            }
        }
    }

}
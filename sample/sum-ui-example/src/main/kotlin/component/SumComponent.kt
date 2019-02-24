package component

import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.events.Event
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.form
import react.dom.input
import react.dom.label
import react.dom.p

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
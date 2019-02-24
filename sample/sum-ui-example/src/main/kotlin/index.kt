import component.MainComponent
import kotlinext.js.require.context
import kotlinext.js.requireAll
import org.w3c.dom.Document
import react.dom.render
import kotlin.browser.document

fun main() {
    requireAll(context("src", true, js("/\\.css$/")))

    document.ready {
        render(document.getElementById("root")) {
            child(MainComponent::class) {}
        }
    }
}

fun Document.ready(handler: () -> Unit) {
    document.addEventListener("DOMContentLoaded", { handler() })
}
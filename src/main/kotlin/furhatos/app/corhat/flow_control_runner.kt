package furhatos.app.corhat

import furhatos.flow.kotlin.FlowControlRunner
import furhatos.util.random

fun FlowControlRunner.randomLambda(vararg lambdas: () -> Unit) {
    val result = ArrayList<() -> Unit>()
    for (l in lambdas) // is an Array
        result.add(l)
    randomLambda(result)
}

fun FlowControlRunner.randomLambda(list: List<() -> Unit>) {
    val f = randomItem(list)
    if (f != null) {
        f()
    }
}

fun <T> FlowControlRunner.randomItem(vararg items: T): T? {
    val result = ArrayList<T>()
    for (i in items) // items is an Array
        result.add(i)
    return randomItem(result)
}

fun <T> FlowControlRunner.randomItem(list: List<T>): T? {
    val f = list.random()
    return f
}

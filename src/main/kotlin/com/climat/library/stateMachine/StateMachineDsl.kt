package com.climat.library.stateMachine

internal fun <TState, TEvent> stateMachine(initState: TState, config: StateMachine<TState, TEvent>.() -> Unit): StateMachine<TState, TEvent> {
    val sm = StateMachine<TState, TEvent>(initState)
    config(sm)
    return sm
}

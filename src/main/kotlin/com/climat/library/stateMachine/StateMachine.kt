package com.climat.library.stateMachine

internal class StateMachine<TState, TEvent>(
    initState: TState
) {

    private var currentState: State
    private val stateMap = mutableMapOf<TState, State>()

    val current: TState get() = currentState.state

    init {
        currentState = State(initState)
        stateMap[initState] = currentState
    }

    fun next(event: TEvent) {
        val handlers = currentState.getHandlers(event)
        requireNotNull(handlers) { "No handlers defined for $event" }
        handlers.forEach { it(currentState) }
    }

    fun state(state: TState, scope: State.() -> Unit) {
        scope(
            stateMap.getOrPut(state) {
                State(state)
            }
        )
    }

    fun transitionTo(target: TState) {
        require(target in stateMap) { "State $target is not defined" }
        currentState = stateMap[target]!!
    }

    internal inner class State(val state: TState) {

        private val eventHandlers = mutableMapOf<TEvent, MutableList<State.() -> Unit>>()

        fun on(event: TEvent, handler: State.() -> Unit) {
            getHandlersOrEmpty(event).add(handler)
        }
        private fun getHandlersOrEmpty(event: TEvent) = eventHandlers.getOrPut(event) {
            mutableListOf<State.() -> Unit>()
        }

        fun getHandlers(event: TEvent) = eventHandlers[event]
    }
}

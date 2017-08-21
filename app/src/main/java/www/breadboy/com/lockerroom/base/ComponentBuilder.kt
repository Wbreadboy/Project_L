package www.breadboy.com.lockerroom.base

/**
 * Created by SDG on 2017. 8. 22..
 */

interface ComponentBuilder<M: BaseModule<*>, C: BaseComponent<*>> {

    fun module(module: M): ComponentBuilder<M, C>
    fun build(): C
}
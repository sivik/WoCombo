package com.example.wocombo.common.extensions

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (Fragment) -> T,
    val cleanUp: ((T?) -> Unit)?
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver =
                Observer<LifecycleOwner?> {
                    val viewLifecycleOwner = it ?: return@Observer

                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            cleanUp?.invoke(binding)
                            binding = null
                        }
                    })
                }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>
    ): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED).not()) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef).also { this.binding = it }
    }
}

inline fun <T : ViewBinding> Fragment.viewBinding(
    crossinline viewBindingFactory: (View) -> T,
    noinline cleanUp: ((T?) -> Unit)? = null,
): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate(
        this,
        { f -> viewBindingFactory(f.requireView()) },
        cleanUp
    )

inline fun <T : ViewBinding> Fragment.viewInflateBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
    noinline cleanUp: ((T?) -> Unit)? = null
): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate(this, { f ->
        bindingInflater(f.layoutInflater)
    }, cleanUp)

inline fun <T : ViewBinding> AppCompatActivity.viewInflateBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }
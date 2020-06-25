package edu.hm.foodweek.util.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <X, Y> LiveData<X>.map(func: (X?) -> Y?): MutableLiveData<Y?> {
    return MediatorLiveData<Y>().apply {
        addSource(this@map) { x -> value = func(x) }
    }
}

fun <X, Y> LiveData<X>.mapSkipNulls(func: (X) -> Y): MutableLiveData<Y> {
    return MediatorLiveData<Y>().apply {
        addSource(this@mapSkipNulls) { x ->
            x ?: return@addSource
            value = func(x)
        }
    }
}

fun <T> LiveData<T>.debounce(duration: Long = 1000L, coroutineScope: CoroutineScope) =
    MediatorLiveData<T>().also { mld ->
        val source = this
        var job: Job? = null

        mld.addSource(source) {
            job?.cancel()
            job = coroutineScope.launch {
                delay(duration)
                mld.value = source.value
            }
        }
    }

fun <A, B> LiveData<A>.combineLatest(b: LiveData<B>): LiveData<Pair<A, B>> {
    return MediatorLiveData<Pair<A, B>>().apply {
        var lastA: A? = null
        var lastB: B? = null

        addSource(this@combineLatest) {
            if (it == null && value != null) value = null
            lastA = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }

        addSource(b) {
            if (it == null && value != null) value = null
            lastB = it
            if (lastA != null && lastB != null) value = lastA!! to lastB!!
        }
    }
}
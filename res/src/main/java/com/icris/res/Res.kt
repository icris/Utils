package com.icris.res

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.min
import kotlin.reflect.KProperty
import kotlin.time.Duration


/**
 * 资源 Resource
 * 调用 get 时触发加载
 * load 报错或返回 null 会在下次调用 get 时重新加载
 *
 * @param expireIn 过期时间，过期自动刷新
 * @param autoReload 自动刷新，默认不刷新
 * @param load 加载函数
 */
@Suppress("unused")
class Res<T>(
    private val expireIn: Duration = Duration.INFINITE,
    private val autoReload: Boolean = false,
    private val load: suspend () -> T?
) {
    private var _data by mutableStateOf<T?>(null)
    private var _loading by mutableStateOf(false)
    private val mutex = Mutex()
    private var expiredAt: Long = -1
    val isLoading get() = _loading
    private val isExpired get() = expiredAt > 0 && expiredAt < System.currentTimeMillis()
    private val needLoad get() = !_loading && (_data == null || isExpired)
    private val delayTime get() = (expiredAt - System.currentTimeMillis()).coerceAtLeast(100)
    private suspend fun realLoad() = mutex.withLock {
        if (_data == null || isExpired) {
            withLoading {
                val t = load()
                if (t != null) {
                    _data = t
                    if (expireIn.isFinite()) {
                        expiredAt = System.currentTimeMillis() + expireIn.inWholeMilliseconds
                    }
                } else {
                    if (expireIn.isFinite()) {
                        expiredAt = System.currentTimeMillis() + min(10000, expireIn.inWholeMilliseconds)
                    }
                }
            }
        }
        _data
    }
    @Composable operator fun getValue(thisObj: Any?, property: KProperty<*>) = get()

    @Composable
    fun get(): T? {
        if (autoReload && expireIn.isFinite()) {
            // 自动刷新
            LaunchedEffect(expireIn) {
                Log.d("TIME", "Here comes new challenger")
                while (isActive) {
                    realLoad()
                    delay(delayTime)
                }
            }
        } else {
            // 按需加载
            val scope = rememberCoroutineScope()
            SideEffect {
                if (needLoad) scope.launch { realLoad() }
            }
        }
        return _data
    }

    fun asFlow() = flow {
        emit(realLoad())
        if (autoReload && expireIn.isFinite()) {
            while (currentCoroutineContext().isActive) {
                delay(delayTime)
                emit(realLoad())
            }
        }
    }
    suspend fun refresh() = mutex.withLock {
        withLoading {
            _data = load()
        }
    }

    private inline fun withLoading(block: () -> Unit) {
        _loading = true
        try {
            block()
        } catch (e: Throwable) {
            Log.e("Res", "loading error", e)
        } finally {
            _loading = false
        }
    }
}

@Composable
fun <T> StateFlow<Res<T>?>.get(): T? = collectAsState().value?.get()

@Composable operator fun <T> StateFlow<Res<T>?>.getValue(thisObj: Any?, property: KProperty<*>): T? = collectAsState().value?.get()

@Suppress("unused")
@OptIn(ExperimentalCoroutinesApi::class)
inline fun <P, T> Flow<P>.mapRes(
    scope: CoroutineScope,
    expireIn: Duration = Duration.INFINITE,
    autoReload: Boolean = false,
    crossinline block: suspend (P) -> T?
) = mapLatest { Res(expireIn, autoReload) { block(it) } }.stateIn(scope, SharingStarted.Lazily, null)
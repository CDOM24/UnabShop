package me.carlosoliva.unabshop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarProductos() {
        _loading.value = true
        repo.obtenerProductos { lista ->
            _productos.value = lista
            _loading.value = false
        }
    }

    fun agregarProducto(producto: Producto, onResult: (Boolean) -> Unit) {
        _loading.value = true
        repo.agregarProducto(producto) { ok, _ ->
            _loading.value = false
            if (ok) cargarProductos()
            onResult(ok)
        }
    }

    fun eliminarProducto(id: String, onResult: (Boolean) -> Unit) {
        _loading.value = true
        repo.eliminarProducto(id) { ok ->
            _loading.value = false
            if (ok) cargarProductos()
            onResult(ok)
        }
    }
}


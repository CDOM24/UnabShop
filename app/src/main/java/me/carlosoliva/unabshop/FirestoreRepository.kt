package me.carlosoliva.unabshop



import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class FirestoreRepository {

    private val db = Firebase.firestore
    private val coleccion = db.collection("productos")

    fun agregarProducto(producto: Producto, onComplete: (Boolean, String?) -> Unit) {
        coleccion.add(producto)
            .addOnSuccessListener { doc ->
                onComplete(true, doc.id)
            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }

    fun obtenerProductos(onResult: (List<Producto>) -> Unit) {
        coleccion.get()
            .addOnSuccessListener { result ->
                val productos = result.map { doc ->
                    doc.toObject(Producto::class.java).copy(id = doc.id)
                }
                onResult(productos)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun eliminarProducto(id: String, onComplete: (Boolean) -> Unit) {
        coleccion.document(id).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }
}

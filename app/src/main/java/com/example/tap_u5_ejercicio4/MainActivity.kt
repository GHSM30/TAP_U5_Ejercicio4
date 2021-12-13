package com.example.tap_u5_ejercicio4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val baseRemota = FirebaseFirestore.getInstance()
    val lista = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cargarDatosDesdeNube()




        button.setOnClickListener{
            val documento = hashMapOf(
                "nombre" to nombre.text.toString(),
                "telefono" to telefono.text.toString(),
                "edad" to edad.text.toString().toInt()
            )
            baseRemota.collection("TAP")
                .add(documento as Any)
                .addOnSuccessListener {
                    //si funciono
                    AlertDialog.Builder(this)
                        .setMessage("EXITO SE INSERTO")
                        .setTitle("ATENCION")
                        .setPositiveButton("OK", {d, i-> })
                        .show()
                }
                .addOnFailureListener{
                    //no funciono
                    AlertDialog.Builder(this)
                        .setMessage("AL PARECER NO SE PUDO INSERTAR")
                        .setTitle("ERROR")
                        .setPositiveButton("OK", {d, i-> })
                        .show()
                }
            nombre.setText("")
            telefono.setText("")
            edad.setText("")

        }
    }

    fun cargarDatosDesdeNube(){
        baseRemota.collection("TAP")
            .addSnapshotListener {value, error ->
                if(error!= null){
                    AlertDialog.Builder(this)
                        .setMessage("AL PARECER NO SE PUDO REALIZAR CONSULTA")
                        .setTitle("ERROR")
                        .setPositiveButton("OK", {d, i-> })
                        .show()
                    return@addSnapshotListener
                }
                lista.clear()
                for(documento in value!!){
                    var cadena = "NOMBRE: "+documento.getString("nombre")+"\nTELEFONO: "+
                            documento.getString("telefono")+"\nEDAD: "+ documento.get("edad").toString()
                    lista.add(cadena)
                }
                listadocumentos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,lista)

            }
    }

}
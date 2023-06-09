package com.mda.ateinspeccion.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mda.ateinspeccion.R
import com.mda.ateinspeccion.databinding.ActivityScanCiudadanoBinding
import com.mda.ateinspeccion.model.IapiService
import com.mda.ateinspeccion.model.checkinternet1
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class Scan_ciudadano : AppCompatActivity() {
    private lateinit var   binding: ActivityScanCiudadanoBinding
    private lateinit var datos:String
    private lateinit var tolls: Toolbar

    private var Estado= ""
    private var Lic_func= ""
    private var Nombre_Razon= ""
    private var direccion= ""
    private var zona= ""
    private var Num_Res= ""
    private var  Num_Exp= ""
    private var  Giro= ""
    private var  Area= ""
    private var  Fecha_Exp= ""
    private var  Fecha_Caducidad= ""

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Bienvenido")
        builder.setMessage("Recuerda verificar y estar atento a la fecha de vigencia")
        builder.setPositiveButton("Aceptar"){
            dialog, which ->
            Toast.makeText(this,"has aceptado", Toast.LENGTH_LONG).show()
        }
        builder.show()

        binding = ActivityScanCiudadanoBinding.inflate(layoutInflater)

        setContentView(binding.root)
        tolls = findViewById(R.id.topAppBar2)
        binding.fecharesult1.setText("")
        binding.constraintLayout3.setBackgroundResource(R.drawable.btn4)
        //getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_left_24)
        //getSupportActionBar()?.setBackgroundDrawable(ColorDrawable(getResources().getColor(android.R.color.transparent)));
        tolls.setNavigationOnClickListener(){

            val passwordLayout1 =findViewById<TextInputLayout>(R.id.textInputLayout)
            passwordLayout1.error = null
            val passwordLayout: TextInputLayout =findViewById(R.id.textField)
            passwordLayout.error = null
            finish()

        }
        binding.btnlupa.setOnClickListener(){
            binding.fecharesult1.setText("")
            binding.constraintLayout3.setBackgroundResource(R.drawable.btn4)

            //initScan()
        }

        binding.btnlupa1.setOnClickListener(){
            //Toast.makeText(this , "date", Toast.LENGTH_SHORT).show()
            binding.fecharesult1.setText("")
            binding.constraintLayout3.setBackgroundResource(R.drawable.btn4)
            buscarCertificado(binding.etNumeros.text.toString())

        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home->{
                finish()
                true
            }
            else ->super.onOptionsItemSelected(item)
        }

    }

    @SuppressLint("RestrictedApi")
    private  fun initScan(){
        hideKeyboard(currentFocus ?: View(this))

        //IntentIntegrator(this).initiateScan()
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val resultado = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if(resultado != null){
            if(resultado.contents == null){
                Toast.makeText(this,"Cancelado", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Valor del scanner ${resultado.contents}", Toast.LENGTH_SHORT).show()
                datos = resultado.contents
                buscarCertificado(datos)


            }
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }*/

    //val activityLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult())

            @SuppressLint("ResourceAsColor", "RestrictedApi")
            private fun buscarCertificado(dato:String){

                val check= checkinternet1()
                if(check.checkForInternet(this)) {
                    hideKeyboard(currentFocus ?: View(this))

                    getRetrofit()
                    var Lic_func1: String? = ""
                    var url1 = "certificados_apps/conexiones_php/consultar.php?LIC=$dato"
                    val select = binding.Rgroup.getCheckedRadioButtonId()
                    if (select == binding.r1.id) {
                        //url1="certificados_apps/conexiones_php/consultarindeter.php?LIC=$dato"

                        Lic_func1 = dato.padStart(4, '0')
                        url1 = "certificados_apps/conexiones_php/consultar.php?LIC=$Lic_func1"


                    } else {
                        Lic_func1 = dato.padStart(10, '0')
                        url1 =
                            "certificados_apps/conexiones_php/consultarindeter.php?LIC=$Lic_func1"


                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        GlobalScope.launch {
                            val result =
                                getRetrofit().create(IapiService::class.java).getDataCert(url1)
                            //     val result = getRetrofit().create(apiService::class.java). getDataCert(dato)

                            val certpar = result.body()
                            runOnUiThread {
                                if (result != null) {
                                    // Checking the results
                                    Log.d("ayush: ", result.body().toString())
                                    Estado = certpar?.Estado ?: "No exite en base de datos"
                                    Lic_func = certpar?.Lic_Func ?: "No exite en base de datos"
                                    Nombre_Razon =
                                        certpar?.Nombre_Razón_Social ?: "No exite en base de datos"
                                    direccion = certpar?.Direccion ?: "No exite en base de datos"
                                    zona = certpar?.Zona_Urbana ?: "No exite en base de datos"
                                    Num_Res = certpar?.Num_Res ?: "No exite en base de datos"
                                    Num_Exp = certpar?.Num_Exp ?: "No exite en base de datos"
                                    Giro = certpar?.Giro ?: "No exite en base de datos"
                                    Area = certpar?.Area ?: "No exite en base de datos"
                                    Fecha_Exp = certpar?.Fecha_Exp ?: "No exite en base de datos"
                                    Fecha_Caducidad =
                                        certpar?.Fecha_Caducidad ?: "No exite en base de datos"
                                    //if (int1 != null) {
                                    //    Toast.makeText(applicationContext, int1, Toast.LENGTH_SHORT).show()
                                    //}
                                    //val int2:Int?=dato.toInt()
                                    //Lic_func.equals(dato)
                                    //int1==int2

                                    if (Lic_func.equals(Lic_func1)) {
                                        Toast.makeText(
                                            applicationContext,
                                            Lic_func1 + "Licencia_input",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        if (Estado.equals("VIGENTE")) {
                                            binding.constraintLayout3.setBackgroundResource(R.drawable.estadoactivo)
                                            //binding.estadocertifi.setTextColor(R.color.white)
                                            binding.fecharesult1.setText(Fecha_Exp)
                                            val passwordLayout: TextInputLayout =
                                                findViewById(R.id.textInputLayout)
                                            passwordLayout.error = null
                                        } else {
                                            binding.constraintLayout3.setBackgroundResource(R.drawable.estadoinactivo)
                                            binding.fecharesult1.setText(Fecha_Exp)
                                            val passwordLayout: TextInputLayout =
                                                findViewById(R.id.textInputLayout)
                                            passwordLayout.error = null
                                        }

                                    } else {
                                        val passwordLayout: TextInputLayout =
                                            findViewById(R.id.textInputLayout)
                                        passwordLayout.error = "Datos incorrectos"
                                    }



                                    binding.estadoresult.setText(Estado)
                                } else
                                    Toast.makeText(
                                        applicationContext,
                                        "No se recibe ningun",
                                        Toast.LENGTH_SHORT
                                    ).show()
                            }


                        }
                    }
                }else {
                    Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
                }



            }

            private fun getRetrofit(): Retrofit {
                return Retrofit.Builder()
            .baseUrl("https://proyectosti.muniate.gob.pe/")
            //.baseUrl("https://delorekbyrnison.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
       }






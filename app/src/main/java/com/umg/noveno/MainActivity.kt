package com.umg.noveno

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var tachados: IntArray

    private  var casillasArray = arrayListOf<ImageView>()
    private  var casillasUnused = arrayListOf<ImageView>()

    private  var playerOne = arrayListOf<Int>()
    private  var playerTwo = arrayListOf<Int>()
    private  var arrayWinner = arrayListOf<ArrayList<Int>>()

    private var userWin = 0;
    private var cpuWin = 0;


    /**
     * Metodo que se ejecuta cuando se crea la actividad
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       //Iniciamos el array casillas que identifica cada casilla y la almacena en el array
       // casillas = IntArray(9)
        casillasArray.add(a1 as ImageView)
        casillasArray.add(a2 as ImageView)
        casillasArray.add(a3 as ImageView)
        casillasArray.add(b1 as ImageView)
        casillasArray.add(b2 as ImageView)
        casillasArray.add(b3 as ImageView)
        casillasArray.add(c1 as ImageView)
        casillasArray.add(c2 as ImageView)
        casillasArray.add(c3 as ImageView)




        fab.setOnClickListener {
            casillasUnused = arrayListOf()
            casillasArray.forEach {
                it.isEnabled = true
                it.setImageDrawable(getDrawable(R.drawable.casilla))
                casillasUnused.add(it)
            }
            playerOne = arrayListOf()
            playerTwo = arrayListOf()
            current = false
            humano = true
            winnerUser = false
            imageView.visibility = View.GONE
        }

        fab.imageTintList = ColorStateList.valueOf(Color.WHITE)


        casillasArray.forEachIndexed{ index, element ->
            casillasUnused.add(element)
            element.setOnClickListener {
                clickCasilla(element,index)
            }
        }
    }

    var current : Boolean = false;
    var humano : Boolean = true;
    var winnerUser : Boolean = false;

    fun clickCasilla(view: View, index : Int){

        if(!winnerUser){
            view as ImageView
            if(current){
                /*val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                //starting the animation
                view.startAnimation(animation)
                view.setImageDrawable(getDrawable(R.drawable.aspa))*/
                val rotate = RotateAnimation(
                    0f,
                    180f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                )
                rotate.duration = 300
                rotate.interpolator = LinearInterpolator()
                view.startAnimation(rotate)
                view.setImageDrawable(getDrawable(R.drawable.aspa))
                view.isEnabled = false
                playerTwo.add(index)
            }else{
                view.setImageDrawable(getDrawable(R.drawable.circulo))
                val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                view.startAnimation(animation)
                view.isEnabled = false
                playerOne.add(index)
            }
            current = !current

            casillasUnused.remove(view)
            println("REMOVIENOD_${view.id}")

            if(humano){
                Handler().postDelayed({
                    humano = false
                    if(arrayWinner.isNotEmpty() && playerOne.isNotEmpty()){

                        var sK = false
                        var mejor = arrayListOf<Int>()


                        arrayWinner.forEach {
                            if(playerOne == it.subList(0,it.size-1) && !sK){
                                sK = true
                                mejor = it
                            }
                        }

                        var sKC = false
                        var mejorC = arrayListOf<Int>()

                        arrayWinner.forEach {
                            if(playerTwo == it.subList(0,it.size-1) && !sKC){
                                sKC = true
                                mejorC = it
                            }
                        }
                        if(sKC){
                            mejorC.last()
                            var psk = false
                            mejorC.forEach {
                                if(!psk){
                                    var p = casillasArray.get(it)
                                    if(casillasUnused.contains(p)){
                                        p.callOnClick()
                                        psk=true;
                                    }

                                }
                            }
                        }else{
                            if(mejor.isNotEmpty()){
                                var skyp = false;
                                mejor.forEach {
                                    if(!playerOne.contains(it) && !skyp){
                                        var p = casillasArray.get(it)
                                        if(casillasUnused.contains(p)){
                                            p.callOnClick()
                                            skyp=true;
                                        }

                                    }
                                }

                                if(!skyp && casillasUnused.isNotEmpty()){
                                    println("AQUI_0")
                                    var random = casillasUnused.random()
                                    random.callOnClick()
                                }
                            }else{
                                if(casillasUnused.isNotEmpty()){
                                    println("AQUI_2")
                                    var random = casillasUnused.random()
                                    random.callOnClick()
                                }
                            }
                        }





                    }else{
                        if(casillasUnused.isNotEmpty()){
                            println("AQUI_3")
                            var random = casillasUnused.random()
                            random.callOnClick()
                        }
                    }

                }, 200)
            }else{
                humano = true
            }
            validarGanador()
        }
    }

    fun validarGanador(){
        val combinaciones = arrayListOf(
            arrayListOf(0, 1, 2),
            arrayListOf(3, 4, 5),
            arrayListOf(6, 7, 8),
            arrayListOf(0, 3, 6),
            arrayListOf(1, 4, 7),
            arrayListOf(2, 5, 8),
            arrayListOf(0, 4, 8),
            arrayListOf(2, 4, 6)
        )



        if(playerOne.size>2){
            var countC = 0;
            combinaciones.forEach {
                var winer = 0;
                it.forEach { et->
                    if(playerOne.contains(et)){
                        winer++
                    }
                    if(winer==3){
                        //tach_0.visibility = View.VISIBLE
                        println("GANADOR XXXXXX")
                        userWin++
                        user_id.setText("$userWin")
                        Glide.with(this@MainActivity).load(R.drawable.winner).into(imageView)
                        imageView.visibility = View.VISIBLE
                        var eq = false
                        arrayWinner.forEach {
                            if(it.equals(playerOne)){
                                eq = true
                            }
                        }
                        if(!eq){
                            arrayWinner.add(playerOne)
                        }
                        println(arrayWinner)
                        winnerUser = true
                    }
                }
                countC++
            }
        }

        if(playerTwo.size>2){
            combinaciones.forEach {
                var winer = 0;
                it.forEach { et->
                    if(playerTwo.contains(et)){
                        winer++
                    }
                    if(winer==3){
                        cpuWin++
                        cpu_id.setText("$cpuWin")
                        //Toast.makeText(this@MainActivity,"Ganador X2", Toast.LENGTH_LONG).show()
                        Glide.with(this@MainActivity).load(R.drawable.loser).into(imageView)
                        imageView.visibility = View.VISIBLE
                        winnerUser = true
                    }
                }
            }
        }


    }


    fun tachar(n: Int) {
        val tachado: ImageView = findViewById(tachados[n])
        tachado.visibility = View.VISIBLE
        val animation_alpha = AlphaAnimation(0f, 1f)
        animation_alpha.duration = 225
        tachado.startAnimation(animation_alpha)
    }
}
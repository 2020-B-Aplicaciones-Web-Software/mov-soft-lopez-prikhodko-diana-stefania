import java.util.*
import kotlin.collections.ArrayList

fun main () {
    //println("Hola mundo")

    //Duck typing

    var edadProfesor = 31
    // var edadProfesor: Int = 31
    var sueldoProfesor = 12.1
    // var sueldoProfesor : Double = 12.1
    // edadProfesor = "asd" --> Sabe que es entero
    // por ende no permitirá esta acción

    // Variables Mutables
    var edadCachorro = 0
    edadCachorro = 1
    edadCachorro = 2
    edadCachorro = 3

    // Variables Inmutables
    val numeroCedula = 1720817093
    //numeroCedula = 1

    //Tipos de variables
        //Primitivas
        val nombreD : String = "Juan"
        val sueldo : Double = 12.2
        val estadoCivil : Char = 'S'
        val casado : Boolean = false
        val fechaNacimiento: Date = Date()

    //Condicionales

    if(true){
        //Condicion positiva
    }else {
        //Condicion negativa
    }

    when(estadoCivil){
        ('C') -> {
            //println("Huir")
        }
        ('S') -> {
           // println("Conversar")
        }
        ('N') -> {
           // println("Nada")
        }
        else -> {
           // println("No tiene estado civil")
        }
    }

    // Un if-else en una misma línea

    val sueldoMayorAEstablecido = if (sueldo > 12.2) 500 else 0

    //imprimirNombre("Diana")
    calcularSueldo(100.00)
    calcularSueldo(100.00, 14.00)
    calcularSueldoNull(100.00, 14.00, 25.00)

    //Parametros nombrados

    calcularSueldoNull(
        sueldo = 123.00,
        bono = 3.00,
        tasa = 3.00
    )

    //Arreglo estático: no podemos aumentar o eliminar elementos
    val ArregloEstatico: Array<Int> = arrayOf(1,2,3)
    //ArregloEstatico.add(12)

    //Areglo dinámico: podemos aumentar o eliminar elementos
    val arregloDinamico: ArrayList<Int> = arrayListOf(1,2,3,4,5,6,7,8,9,10)
   // println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    //println(arregloDinamico)

    //Operadores --> Sirven para los arreglos estáticos y dinámicos
    // Funciones que sirven para cualquier tipo de iterable
    // Para eliminar el uso de while, for

    // foreach
    val respuestaForEach : Unit = arregloDinamico
        .forEach { valorActual : Int ->
           // println("Valor actual: ${valorActual}")
        }

    //Si tenemos solo un parametro y queremos reducir
/*
    arregloDinamico.forEach{ it
        println("Valor: ${it}")
    }

    arregloDinamico.forEach{ println("V: ${it}")}

    // For Each Indexado

    arregloDinamico.forEachIndexed { indice: Int, valorActual : Int ->
        println("Valor actual: ${valorActual}, Indice: ${indice}")
    }
*/

    // MAP

    val respuestaMap : List<Double> = arregloDinamico
        .map{ valorActual: Int ->
            return@map valorActual.toDouble()
        }
  //  println(respuestaMap)

    val respuestaMapDos : List<Double> = arregloDinamico
        .map { valorActual : Int ->
            return@map valorActual.toDouble() + 100.00
        }
   // println(respuestaMapDos)

    val respuestaMapTres : List<Date> = arregloDinamico
        .map { valorActual : Int ->
            return@map Date()
        }
   // println(respuestaMapTres)


    //filter

    val respuestaFilter : List<Int> = arregloDinamico
        .filter { valorActual : Int ->
            val mayorACinco : Boolean = valorActual > 5 // Expresion true o false
            return@filter mayorACinco
        }
    //println(respuestaFilter)

    val respuestaFilterDos : List<Int> = arregloDinamico
        .filter { valorActual : Int ->
            val menorACinco : Boolean = valorActual <= 5 // Expresion true o false
            return@filter menorACinco
        }
    //println(respuestaFilterDos)

    // Operadores
    // OR -> (Si alguno cumple -> es verdadero) -> ANY

    val respuestaAny : Boolean = arregloDinamico
        .any{ valorActual: Int ->
            return@any (valorActual > 5)
        }

   // println("Respuesta del any: " + respuestaAny)

    // AND -> (Si cumplen todos -> es verdadero) -> ALL

    val respuestaAll : Boolean = arregloDinamico
        .all{ valorActual: Int ->
            return@all (valorActual > 5)
        }

    //println("Respuesta del all: " + respuestaAll)

    //Reduce -> Valor Acumulado
    //Valor acumulado = 0 (Siempre 0 en lenguaje Kotlin)
    // [1,2,3,4,5] -> Sumar todos los valores del arreglo
    //ValorIteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion1
    //ValorIteracion2 = ValorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion2
    // ...

    val respuestaReduce : Int = arregloDinamico
        .reduce { acumulado: Int , valorActual : Int ->
            return@reduce (acumulado + valorActual)
        }
   // println("Valor suma Reduce: " + respuestaReduce)

    // En un juego, el heroe tiene 100 puntos de vida
    // Vamos a crear un arreglo con el daño que fue hecho al heroe
    // Operador Fold
    val arregloDanio : ArrayList<Int> = arrayListOf<Int>(12,15,8,10)
    val respuestaReduceFold = arregloDanio
        .fold( 100
        ) { acumulado, valorActualInteracion ->
            return@fold acumulado - valorActualInteracion
        }
    //println("Una vez reducida la vida: " + respuestaReduceFold)

    //usar todos los operadores concatenado

    val vidaActual : Double = arregloDinamico
        .map{ it * 2.3 } //el daño se pontencia en un 2.3
        .filter{ it > 20 } // un escudo que protege de los ataques de 0 - 19
        .fold(100.0, {acumulador , iteracion -> acumulador - iteracion}) //Acumulador inverso
        // Reduciendo la vida el heroe
        .also { println("Valor del it: " + it) } //Manera de imprimir el resultado
    //println("Vida resultante: ${vidaActual}")

    val ejemploUno = Suma(1,2)
    val ejemploDos = Suma(null, 2)
    val ejemploTres = Suma(1,null)
    val ejemploCuatro = Suma(null, null)

    println(ejemploUno.sumar())
    println(ejemploDos.sumar())
    println(ejemploTres.sumar())
    println(ejemploCuatro.sumar())

    //Para acceder de manera directa, se lo hace como una propiedad
    println(Suma.historialSumas)

    //De la misma manera a los metodos del compain

    Suma.agregarHistorial(20)
    println(Suma.historialSumas)

} //Fin main

// Cuando no devuelves algo se pone --> Unit
fun imprimirNombre(nombre: String): Unit{
    println("Nombre ${nombre}")
}
//El segundo es opcional, ya que lo declaramos
fun calcularSueldo(sueldo: Double,tasa: Double = 12.00) : Double {
    return sueldo * (100 / tasa)
}

//Parametros que pueden ser null --> eliminar Null Point Exception
fun calcularSueldoNull(sueldo: Double,tasa: Double = 12.00, bono: Double? = null) : Double {
    if (bono != null){
        return sueldo * (100 / tasa) + bono
    }else {
        return sueldo * (100 / tasa)
    }
}

abstract class NumeroJava{
    //Se los puede inicializar fuera o dentro del constructor
    protected val numeroUno: Int //Propiedades clase, que poseen
    private val numeroDos: Int //  modificadores: private, protected, public

    // Sintaxis del constructor
    //contructor(Parametros requeridos)
    constructor(uno: Int, dos: Int){
//        this.numeroUno =  uno     --> Modo Java
//        this.numeroDos = dos      --> Modo Java
        numeroUno = uno
        numeroDos = dos
    }
}

abstract class NumerosKotlin( //Constructor primario
    protected var numeroUno: Int, //Propiedades clase
    protected var numeroDos: Int //Propiedades clase
){
    init { //Bloque inicio del constructor primario
        println("Inicializar")
    }
}

class Suma( //Contructor primario
    uno: Int, //Parametro requerido
    dos: Int, //Parametro requerido
): NumerosKotlin( //Constructor del padre (supercontructor)
    uno, dos
) {
    init {
        this.numeroUno
        this.numeroDos
    }
    constructor( //Segundo contructor
        uno: Int?, //parametros
        dos: Int  // parametros
    ): this( //llamado al primer constructor
        if(uno == null) 0 else uno,
        dos
    )
    constructor(//Tercer constructor
        uno: Int, //parametros
        dos: Int? //parametros
    ): this( //llamado al primer constructor
        uno,
        if(dos == null) 0 else dos,
    )
    constructor(//Cuarto constructor
        uno: Int?, //parametros
        dos: Int? //parametros
    ): this(//llamado al primer constructor
        if(uno == null) 0 else uno,
        if(dos == null) 0 else dos,
    )

    fun sumar():Int {
        //No es necesario colocar los this
        val total: Int = numeroDos + numeroUno
        agregarHistorial(total)
        return  total
    }

    //Singleton
    //No se va a instanciar varias veces, solo una vez
    companion object{
        val historialSumas: ArrayList<Int> = arrayListOf<Int>()
        fun agregarHistorial(valorNuevoSuma:Int){
            historialSumas.add(valorNuevoSuma)
            println("Historial actual: ${historialSumas}")
        }
    }

}


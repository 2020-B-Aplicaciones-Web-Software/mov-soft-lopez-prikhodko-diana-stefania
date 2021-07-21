package com.example.movilessoftware2021aejercicio1

class BBaseDatosMemoria {
    companion object{
        //Propiedades
        //Métodos
        //Estáticos (Singleton de la clase)
        //No es necesario crear una instancia para acceder al companion objetct
        val arregloBEntrenador = arrayListOf<BEntrenador>(

        )
        init {
            arregloBEntrenador
                .add(
                    BEntrenador("Adrian","a@a.com", DLiga("Ana","Liga ana"))
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Juan","as@a.com", DLiga("Ana","Liga ana"))
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Ana","asd@a.com",DLiga("Ana","Liga ana"))
                )
        }

    }
}
package com.angelfg.best_travel.util.constants;

public class CacheConstants {
    public static final String FLY_CACHE_NAME = "flights";
    public static final String HOTEL_CACHE_NAME = "hotels";

    // Pagina para saber el tiempo => https://crontab.cronhub.io/
    // 0 0/1 * * * * => Expresion es de ejecucion cada minuto
    public static final String SCHEDULED_RESET_CACHE = "0 0 0 * * ?"; // cada 12 am o media noche
}

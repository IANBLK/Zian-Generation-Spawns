# Validación pendiente en servidor

Las siguientes comprobaciones NO se han ejecutado en el entorno actual.

1. Arrancar NeoForge 1.21.1 + Cobblemon 1.8.1 + dependencias, sin Youer primero. Comprobar ausencia de errores y mensajes de registro de filtros.
2. Mundo nuevo: active vacío; verificar bloqueo natural, pesca Pokémon y Poké Snacks. Confirmar qué ocurre con consumo/reembolso de cebo al cancelar.
3. Abrir gen1; comprobar especies permitidas y bloqueadas en las tres rutas. Usar intentos controlados y registros, no concluir por ausencia casual de spawns raros.
4. Abrir gen7: verificar Meltan/Melmetal cuando exista un spawn configurado. Abrir gen8: comprobar especies con etiqueta gen8a; no asumir que una forma regional cambia la generación base.
5. Alternar comandos desde Overworld, Nether, End y consola. Todos deben reflejar la misma lista en active y debug.
6. Guardar, detener limpiamente y arrancar; verificar persistencia. No usar kill -9 como prueba de guardado normal.
7. Reabrir mundos integrados en la misma JVM: no debe haber filtros o suscripciones duplicados.
8. Un jugador sin OP puede consultar active, pero no habilitar/deshabilitar ni usar debug.
9. Probar datapacks con especies desconocidas/sin etiqueta y documentar su bloqueo. Revisar rutas nuevas de aparición de 1.8.1.
10. Verificar que Pokémon capturados, crianza, evolución y GTS conservan el comportamiento esperado.
11. Repetir en la build exacta de Youer elegida, documentando versión, Java, otros mods y logs.

La política gen7b/gen8a, generaciones ordinarias, etiquetas desconocidas y prefijos falsos tiene pruebas JUnit en common/src/test. Consultar Actions para su estado real; no confundir pruebas escritas con pruebas ejecutadas.

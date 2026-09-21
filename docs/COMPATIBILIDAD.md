# Comparación con el JAR exacto de Cobblemon 1.8.1

Fecha de inspección: 2026-09-21.

| Archivo | SHA-256 |
|---|---|
| cobblemongenerationspawns-neoforge-1.1.0-release.jar | 28432063101871ad06fcb3263345703ab28ab0af5d3883b264b6060572aeb62c |
| Cobblemon-neoforge-1.8.1+1.21.1.jar | 5e6882d60d76f57c56dddbdfb9a66eafdb9f7b93103a305ca51f285b09d20c0d |

El manifiesto del segundo JAR declara 1.8.1+1.21.1, Java 21 y Kotlin for Forge 5.10 o posterior.

## Resultado observado

Las 17 referencias únicas a campos/métodos de Cobblemon en las clases del JAR original se resuelven por nombre y descriptor dentro del JAR aportado, buscando también en superclases/interfaces. Incluyen el evento Poké Snack, los filtros de jugadores y pesca y Species.getLabels() con retorno HashSet.

[Salida reproducible](abi-original-vs-1.8.1.txt). Ejecutar:

```sh
python tools/check_cobblemon_abi.py /ruta/mod-original.jar /ruta/Cobblemon-neoforge-1.8.1+1.21.1.jar
```

El programa es un inventario estático de descriptores, no un enlazador JVM: no verifica todas las reglas de acceso, instrucciones de invocación, dependencias transitivas ni comportamiento. Una referencia encontrada no prueba cobertura del filtro o compatibilidad completa en juego. No hay métodos abstractos nuevos que el mod deba implementar en la interfaz SpawningInfluence inspeccionada: sus métodos de instancia poseen implementación predeterminada.

## Corrección a la recomendación de la auditoría inicial

El JAR contiene etiquetas **gen7b** y **gen8a**. Sustituir startsWith por igualdad sin equivalencias excluiría especies válidas. La política de esta adaptación normaliza exclusivamente gen7b→gen7 y gen8a→gen8, y después exige coincidencia exacta con gen1–gen9. No permite gen10 al abrir gen1.

| Etiqueta | Especies JSON en el JAR |
|---|---:|
| gen1 | 151 |
| gen2 | 100 |
| gen3 | 135 |
| gen4 | 107 |
| gen5 | 156 |
| gen6 | 72 |
| gen7 | 86 |
| gen7b | 2 |
| gen8 | 89 |
| gen8a | 7 |
| gen9 | 120 |

Se inspeccionaron los JSON bajo data/cobblemon/species: ninguno carecía de etiqueta que comenzara por gen. Esto cuenta definiciones de especies, no apariciones habilitadas ni todas sus formas. Los datapacks pueden cambiar el resultado.

## Cambios en la primera adaptación

- Las lecturas/escrituras de SavedData usan siempre Overworld; se conserva el identificador de datos original.
- La caché utiliza snapshots inmutables y se limpia al detener el servidor.
- Se valida la lista al leer NBT y se omiten con aviso los IDs desconocidos.
- Registro de filtros idempotente, sin la falsa espera silenciosa. Hay limpieza de registros y suscripción al detener.
- Natural/pesca y Poké Snacks comparten la regla de selección de especies.
- Se mantienen los pesos de las especies permitidas; se elimina el multiplicador uniforme 9−N del original.
- Alcance de compilación reducido a NeoForge. Se conservan autoría y licencia original.
- Se añaden ocho pruebas JUnit de la política de generaciones.

## Estado de verificación

Inspección binaria y revisión de JSON ejecutadas. El intento local de `:common:test :neoforge:build` no pudo descargar Gradle (Network is unreachable); además, el entorno local solo dispone de Java 17. Por tanto, ninguna prueba Java ni compilación local se declara superada.

GitHub Actions prepara Java 21 y ejecuta las pruebas y la compilación. Consultar el resultado real en Actions; esta documentación no presupone que haya terminado correctamente. Aun con compilación correcta, quedan las pruebas de servidor de PRUEBAS.md, incluida la build exacta de Youer si se utiliza.


### Resultado posterior: compilación de alpha.1

GitHub Actions completó correctamente la compilación el 21-09-2026: ocho pruebas JUnit, cero fallos y cero omitidas. El JAR remapeado se descargó y se verificaron su integridad, metadatos, clases, licencia y ausencia de rutas duplicadas. Sus 17 referencias directas a Cobblemon también se encuentran en el JAR aportado. Ver VERSION_ALPHA_1.md. Las pruebas en servidor y Youer siguen pendientes.

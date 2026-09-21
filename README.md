# Zian Generation Spawns

Adaptación de CobblemonGenerationSpawns de **DAN2026**, mantenida para el proyecto de **ZIANBLK**. Objetivo: Minecraft 1.21.1, NeoForge y Cobblemon 1.8.1.

**Estado: desarrollo, 1.2.0-alpha.1. No validado todavía en un servidor ni en Youer.** La inspección del JAR aportado encuentra las 17 referencias directas del mod original a Cobblemon. Eso no certifica el arranque o la cobertura de todas las formas de aparición.

## Comportamiento

- Lista global de generaciones permitidas, compartida por todas las dimensiones.
- Un mundo nuevo empieza con todas las generaciones bloqueadas, igual que el original.
- Filtros para aparición natural, pesca y Poké Snacks a través de las integraciones originales.
- `gen7` incluye `gen7b` (Let's Go); `gen8` incluye `gen8a` (Hisui).
- Se mantienen los pesos originales de los Pokémon permitidos. No se multiplica artificialmente su peso según cuántas generaciones estén abiertas.
- No bloquea específicamente crianza, evolución, intercambios, GTS ni entregas por comandos.

| Comando | Permiso | Uso |
|---|---|---|
| `/generation enable gen1` | OP nivel 2 | Habilita una generación |
| `/generation disable gen1` | OP nivel 2 | Deshabilita una generación |
| `/generation active` | Todos | Consulta generaciones activas |
| `/generation debug` | OP nivel 2 | Compara caché y datos guardados |
| `/generation help` | OP nivel 2 | Muestra ayuda |

Los comandos aceptan gen1–gen9 y ofrecen autocompletado. Los mensajes originales de comandos todavía están en inglés.

## Compilación

Requiere JDK 21 y conexión a los repositorios Maven y Gradle configurados.

Linux:

```sh
./gradlew :common:test :neoforge:build
```

Windows PowerShell:

```powershell
.\gradlew.bat :common:test :neoforge:build
```

Salida prevista: `neoforge/build/libs/zian-generation-spawns-neoforge-1.2.0-alpha.1.jar`. Usar únicamente el artefacto remapeado, no dev-slim ni dev-shadow. GitHub Actions incluye compilación y pruebas; un resultado correcto de compilación todavía requiere pruebas de juego.

## Migración

Se conserva `cobblemongenerationwaves_data` en los datos del Overworld. Las listas antiguas de otras dimensiones no se borran ni se fusionan automáticamente. Antes de sustituir el mod, guardar una copia del mundo y decidir manualmente qué generaciones deben quedar activas si existían listas distintas.

Instalar solo esta variante o la original; comparten modId. Kotlin for Forge es una dependencia de Cobblemon, y debe cumplir los requisitos de su instalación.

## Revisión y próximos pasos

- [Hallazgos sobre el JAR exacto 1.8.1](docs/COMPATIBILIDAD.md)
- [Pruebas pendientes de servidor](docs/PRUEBAS.md)
- [Mejoras propuestas](docs/PROPUESTAS.md)
- [Autoría y condiciones originales](NOTICE.md)

Código derivado bajo la [licencia original](LICENSE). No se han eliminado los créditos de DAN2026.

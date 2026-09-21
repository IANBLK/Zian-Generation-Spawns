# Zian Generation Spawns 1.2.0-alpha.1

Primera versión compilada para pruebas. Minecraft 1.21.1, NeoForge 21.1.182 o posterior, Cobblemon 1.8.1 y Java 21.

- Commit del código compilado: `6ae2fe90699d34010827a25ccb50db5c5c492c9c`.
- [Ejecución de GitHub Actions](https://github.com/IANBLK/Zian-Generation-Spawns/actions/runs/35574119418).
- [Artefacto neoforge-candidate](https://github.com/IANBLK/Zian-Generation-Spawns/actions/runs/35574119418/artifacts/10626544330).
- JAR: `zian-generation-spawns-neoforge-1.2.0-alpha.1.jar`.
- Tamaño: 35 090 bytes.
- SHA-256: `6c37ebba926d9fdbe8a22a497d3727d6e1d1ab293a9e49cb6a3488294eec91ff`.

## Comprobado

- Compilación `:common:test :neoforge:build`: correcta.
- 8 pruebas automatizadas de reglas de generaciones: superadas, 0 fallos y 0 omitidas.
- Archivo ZIP/JAR íntegro, 18 clases, sin entradas duplicadas; contiene GenerationPolicy, SpawnFactors, punto de entrada NeoForge, LICENSE y NOTICE.md.
- Metadatos: modId `cobblemongenerationspawns`, versión `1.2.0-alpha.1`, dependencias de Minecraft/NeoForge/Cobblemon presentes.
- [17 referencias del nuevo binario a Cobblemon](abi-alpha.1-vs-1.8.1.txt): todas encontradas en el JAR 1.8.1 aportado.

## Instalación para la prueba

1. Guardar una copia del mundo y detener el servidor limpiamente.
2. Retirar el JAR original `cobblemongenerationspawns-neoforge-1.1.0-release.jar` si está instalado. Ambas variantes comparten modId y no deben cargarse juntas.
3. Colocar este JAR en `mods`, junto a Cobblemon 1.8.1 y sus dependencias.
4. Arrancar y comprobar los registros. Ejecutar `/generation active` y `/generation debug` como administrador.
5. Si es un mundo nuevo, habilitar la generación inicial con `/generation enable gen1`; por defecto ninguna está habilitada.
6. Seguir PRUEBAS.md y conservar latest.log si aparece un error.

Se conserva la lista guardada en el Overworld bajo `cobblemongenerationwaves_data`. No se fusionan automáticamente las listas antiguas de otras dimensiones.

## Pendiente

No se ha ejecutado un servidor Minecraft ni Youer en esta revisión. Falta comprobar aparición natural, pesca, Poké Snacks, persistencia tras reinicios y coexistencia con los otros mods/plugins del servidor. La compilación y las pruebas unitarias no sustituyen esas verificaciones. No se etiqueta como versión estable.

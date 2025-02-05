# Glassgame

**Versión:** 1.0  
**Autor:** FroshGames  
**Compatible con:** Minecraft 1.20.1  

Glassgame es un plugin inspirado en *Squid Game*, en el que los jugadores deben cruzar una plataforma con bloques falsos que se romperán al pisarlos. ¡El último en pie gana!

## 📥 Instalación
1. Descarga el archivo `Glassgame.jar`.
2. Coloca el archivo en la carpeta `plugins` de tu servidor Spigot/Paper.
3. Reinicia el servidor.
4. Configura los ajustes en `config.yml` si es necesario.

## ⚙️ Configuración (`config.yml`)
El archivo de configuración permite personalizar la mecánica del juego:

- `block-to-break`: Tipo de bloque falso (por defecto `TINTED_GLASS`).
- `break-delay`: Tiempo en ticks antes de que el bloque se rompa (20 ticks = 1 segundo).
- `search-in-2d`: Si la destrucción de bloques es solo en la capa del bloque o en 3D.
- `kill-player`: Si el jugador muere automáticamente al pisar el bloque falso.
- `restricted-area`: Restringe la mecánica a una zona específica del mundo.

## 🎮 Comandos
| Comando | Descripción |
|---------|------------|
| `/glassgame start` | Inicia el minijuego. |
| `/glassgame stop` | Detiene el minijuego. |
| `/glassgame help` | Muestra la ayuda de comandos. |

## 📜 Permisos
Este plugin no requiere permisos especiales.

## 📌 Notas
- Se recomienda usar este plugin en un entorno de minijuegos o eventos.
- Puedes modificar los mensajes en `config.yml` para personalizar la experiencia.

## 🤝 Créditos
Desarrollado por **FroshGames| Amir Chiquito**.  
Agradecimientos a **MiauluStudios** por su contribución y testeos.

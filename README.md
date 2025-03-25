# Sea Battle

Descripción:
Desarrollo de una aplicación móvil que permita a dos personas entablar una partida online
del juego Hundir la flota (https://es.wikipedia.org/wiki/Batalla_naval_(juego)).


Objetivos de aprendizaje:
- Autenticación de usuarios con Firebase Auth.
- Gestión de usuarios con Firestore y/o Firebase Realtime Database.
- Creación y desarrollo de partidas con Firestore y/o Firebase Realtime Database.
- Desarrollo de interfaces de usuario móvil.
- Manejo, gestión y mantenimiento de estado en la aplicación.


Funcionalidades mínimas:
1. Creación y gestión de cuentas de usuario:
- Registro de usuario (a través de Google/Apple).
- Gestión de un perfil básico (nombre de usuario).
- Opción de cerrar sesión del usuario.

  
2. Gestión de partidas:
- Buscar un usuario ya registrado para iniciar una partida.
- Desarrollo de la partida mostrando los tableros correspondientes a cada usuario con vistas simples nativas.
- Interacción sobre dichos tableros en función a las reglas del juego.
- Determinación de ganador y perdedor de la partida.


Funcionalidades extra:
1. Perfil de usuario avanzado: avatar, nombre de usuario… (Se puede mostrar el nombre de los usuarios en las partidas)
2. Chat asociado a cada partida.
3. Sistema de leaderboards en el que se muestre qué usuario ha ganado más partidas.
4. Implementación de algún tipo de sistema de matchmaking que empareje usuarios para jugar sin tener que buscar uno concreto, siguiendo alguno de estos algoritmos (Aleatorio o en base al número de partidas ganadas)
5. Representación de los tableros de juego con motores 2D o 3D.

El alumno también es libre de añadir otras funcionalidades extra no descritas aquí.


Requisitos tecnológicos:
- Cuenta de Firebase.


Recomendaciones Android:
- Uso de Kotlin como lenguaje de programación.
- Uso de Jetpack Compose para la UI.
- Uso de corrutinas para lógica en segundo plano y gestión de reactividad de la aplicación.

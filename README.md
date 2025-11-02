# PM_Proyecto
----------------------- B'ele'k ----------------------------
En Guatemala, el turismo es muy importante para la economía,
pero la mayoría de visitantes se enfoca solo en los lugares 
más populares. Esto hace que muchos destinos menos conocidos, 
pero igualmente valiosos, pasen desapercibidos, afectando a 
los negocios locales que dependen del turismo. Además, los 
viajeros no siempre encuentran información que se adapte a sus 
gustos o ubicación.

Con esto en mente, creamos "Bele'k", una aplicación totalmente 
enfocada en fortalecer el turismo en Guatemala. Su propósito 
es ofrecer una experiencia personalizada al recomendar tanto
destinos populares como rincones poco conocidos, adaptados a 
los intereses del usuario y a su ubicación. De esta manera, 
Bele'k busca enriquecer cada viaje, promoviendo al mismo tiempo 
el crecimiento de los emprendimientos locales al brindarles 
mayor visibilidad y apoyo dentro del ecosistema turístico 
nacional.

El nombre Bele'k proviene del idioma Q’anjob’al, una de las 
lenguas mayas originarias de Guatemala. En Q’anjob’al, la raíz 
b’e o b’el significa “camino” o “caminar”, mientras que el 
sufijo -ek’ se utiliza para expresar a quien realiza la acción. 
Por ello, Bele'k puede interpretarse como “el que va por el 
camino”, “caminante” o “viajero”. Este nombre fue elegido como 
un símbolo de conexión con las raíces culturales del país y 
como una forma de rendir homenaje a la riqueza lingüística y 
ancestral de Guatemala. Además, busca inspirar orgullo y 
fortalecer el sentido de identidad nacional al promover 
el turismo desde una perspectiva auténticamente guatemalteca.
//////////////////////////////////////////////////////////////

Servicios:
- Google Maps API / Aun no implementado
Se utilizará para mostrar mapas interactivos, ubicaciones 
de lugares turísticos y rutas desde la posición actual del 
usuario.

- Supabase Authentication / Aun no implementado
Permite que los usuarios se registren e inicien sesión de
manera segura mediante correo electrónico. Este servicio 
garantiza una experiencia personalizada y protegida 
dentro de la aplicación.

- Servicio de traducción (Google Cloud Translation API o 
servicio local simulado) / Aun no implementado
Se empleará para ofrecer la posibilidad de cambiar el idioma 
de la aplicación según la preferencia del usuario. En caso 
de no utilizar un servicio real, se implementará una 
funcionalidad interna que simule la traducción de textos 
clave.

- Servicio de conversión de moneda (API de tipo de cambio
(ExchangeRate API o CurrencyFreaks) o servicio 
simulado) / Aun no implementado
Se utilizará para mostrar precios o información relevante 
en diferentes monedas, facilitando la experiencia de los 
turistas extranjeros.

////////////////////////////////////////////////////////////

Librerias:
- Retrofit (Aun no implementad)
Para realizar llamadas HTTP y consumir las APIs externas 
que proveen información sobre destinos, traducciones o 
tipos de cambio.

- Ktor
Para realizar llamadas HTTP de manera más flexible y 
asíncrona, útil especialmente en operaciones de red 
dentro de Kotlin/Android.

- Coil (Aun no implementado)
Para la carga eficiente y optimizada de imágenes desde 
URLs, garantizando un mejor rendimiento y experiencia 
visual dentro de la aplicación.

- Navigation Compose (Implementado)
Para gestionar la navegación entre pantallas de manera 
estructurada y fluida dentro de la app, utilizando los 
principios de Jetpack Compose.

- Room (Aun no implementado)
Para la gestión de la base de datos local, permitiendo 
almacenar información como destinos guardados, historial 
de búsqueda o preferencias del usuario, incluso sin 
conexión a internet.

- Supabase (Aun no implementado)
Para manejar la autenticación y mantener la sesión activa 
del usuario, brindando una experiencia segura y confiable.

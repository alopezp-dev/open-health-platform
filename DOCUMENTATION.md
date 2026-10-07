# OPEN HEALTH PLATFORM

**Open Health Platform (OHP)** es una plataforma sanitaria de código abierto orientada a la gestión e interoperabilidad de información sanitaria.

El sistema está diseñado para poder adaptarse a diferentes escalas y modelos sanitarios, desde una clínica o centro de salud hasta redes hospitalarias, servicios regionales o infraestructuras sanitarias nacionales.

La plataforma pretende ser independiente de un país concreto. Las particularidades de cada territorio, organización o sistema sanitario deberán incorporarse mediante configuración, perfiles y módulos específicos, evitando modificar el núcleo común de la aplicación para cada despliegue.

El proyecto se desarrolla inicialmente tomando como referencia el contexto sanitario europeo y español, pero sin introducir dependencias arquitectónicas que impidan utilizarlo en otros países.

## OBJETIVOS

Open Health Platform pretende proporcionar una base común y extensible para resolver diferentes problemas presentes en los sistemas de información sanitarios actuales.

### Fragmentación de la información sanitaria

La información de un mismo paciente puede encontrarse distribuida entre diferentes organizaciones y sistemas:

- Centro de salud
- Hospital
- Laboratorio
- Radiología
- Farmacia
- Clínica privada
- Otros proveedores sanitarios

El problema no es necesariamente que la información se encuentre distribuida, sino que los diferentes sistemas pueden utilizar modelos, identificadores y tecnologías distintas, dificultando el intercambio y la interpretación de la información.

Open Health Platform deberá favorecer la interoperabilidad entre estos sistemas sin asumir que todos utilizan una única base de datos.

### Duplicación de información

Un mismo paciente puede estar registrado varias veces en distintos sistemas sanitarios y disponer de diferentes identificadores dependiendo de la organización o territorio.

La plataforma deberá permitir representar múltiples identificadores asociados a un mismo registro de paciente y, posteriormente, incorporar mecanismos de gestión y reconciliación de identidad.

### Infraestructura adaptable

La plataforma debe poder funcionar con diferentes modelos de infraestructura.

Por ejemplo, podrá utilizarse:

- En una única organización.
- En varias organizaciones conectadas.
- En una infraestructura regional.
- En una infraestructura nacional.
- En entornos con recursos tecnológicos limitados.

Por tanto, el diseño no debe depender obligatoriamente de una infraestructura centralizada concreta.

### Interoperabilidad

La plataforma deberá poder intercambiar información con otros sistemas sanitarios utilizando estándares internacionales cuando corresponda.

Inicialmente se está utilizando **HL7 FHIR** como referencia para estudiar los conceptos sanitarios y, posteriormente, como estándar de interoperabilidad.

El modelo interno de Open Health Platform **no será una copia directa de FHIR**. Existirá una capa de adaptación entre el dominio interno y los recursos FHIR.

### Escalabilidad y modularidad

La aplicación se diseñará inicialmente como un **monolito modular**, manteniendo separadas las diferentes áreas funcionales.

Esto permitirá comenzar con una infraestructura relativamente sencilla sin impedir que determinados módulos puedan evolucionar o separarse posteriormente si las necesidades de escala lo justifican.

---

# ARQUITECTURA DEL CÓDIGO

La información del sistema se organiza mediante módulos correspondientes a diferentes áreas funcionales.

Actualmente se está desarrollando el primer módulo:

```text
patient
```

Su estructura inicial es:

```text
com.alopezp.openhealth
│
├── OpenHealthPlatformApplication
│
└── patient
    └── domain
        ├── Patient
        ├── PatientId
        ├── PatientIdentityState
        ├── PatientRecordLifecycle
        ├── Identifier
        ├── HumanName
        └── BirthInformation
```

El paquete `domain` contiene los conceptos y reglas pertenecientes al dominio de administración de pacientes.

---

# PATIENT

El módulo `patient` es responsable inicialmente de representar la **identidad administrativa básica de los pacientes**.

No contiene todavía información clínica como diagnósticos, medicamentos, observaciones, citas o episodios asistenciales. Estas responsabilidades pertenecerán a otros módulos.

## DOMAIN

### `Patient`

`Patient` representa un **registro administrativo de paciente** dentro de Open Health Platform.

Es la entidad principal del módulo `patient`.

Actualmente contiene:

```java
private final PatientId id;

private PatientRecordLifecycle lifecycle;

private PatientIdentityState identityState;

private final Set<Identifier> identifiers;

private final List<HumanName> names;

private BirthInformation birthInformation;
```

#### `id`

```java
private final PatientId id;
```

Representa la identidad interna y estable del paciente dentro de Open Health Platform.

No depende de un DNI, tarjeta sanitaria, número hospitalario o cualquier otro identificador externo.

Una vez creado el paciente, este identificador no debe cambiar.

#### `lifecycle`

```java
private PatientRecordLifecycle lifecycle;
```

Representa el estado administrativo del **registro del paciente**.

No indica si la persona está viva, hospitalizada o recibiendo actualmente asistencia.

#### `identityState`

```java
private PatientIdentityState identityState;
```

Representa el estado de conocimiento o verificación de la identidad del paciente.

Permite, por ejemplo, registrar inicialmente un paciente cuya identidad todavía no ha podido ser completamente verificada.

#### `identifiers`

```java
private final Set<Identifier> identifiers;
```

Contiene los identificadores externos conocidos del paciente.

Un mismo paciente puede disponer de identificadores diferentes procedentes de hospitales, sistemas regionales, sistemas nacionales u otras organizaciones.

El conjunto puede estar vacío.

#### `names`

```java
private final List<HumanName> names;
```

Contiene los diferentes nombres conocidos asociados al paciente.

Se utiliza una colección porque una persona puede tener diferentes representaciones de su nombre, como un nombre oficial, habitual, anterior o procedente de diferentes sistemas.

La lista puede estar inicialmente vacía cuando la identidad del paciente todavía sea desconocida.

#### `birthInformation`

```java
private BirthInformation birthInformation;
```

Representará la información conocida sobre el nacimiento del paciente.

Es opcional porque un paciente puede ser registrado inicialmente sin disponer de esta información.

Su modelo todavía se encuentra en diseño debido a que debe permitir representar, además de fechas completas, fechas parciales, desconocidas o estimadas.

---

### `PatientId`

`PatientId` representa el identificador interno único de un registro `Patient` dentro de Open Health Platform.

```java
private final UUID value;
```

El identificador es generado automáticamente por la plataforma:

```text
Open Health Platform
        ↓
    PatientId
        ↓
      UUID
```

Este identificador:

- Es interno a la plataforma.
- Es inmutable.
- No contiene significado clínico.
- No depende de un país.
- No depende de una organización sanitaria externa.

No debe confundirse con `Identifier`.

Un ejemplo conceptual sería:

```text
550e8400-e29b-41d4-a716-446655440000
```

---

### `Identifier`

`Identifier` representa un **identificador externo o administrativo asociado a un paciente**.

Actualmente contiene:

```java
private final String system;
private final String value;
```

#### `system`

```java
private final String system;
```

Representa el **sistema o namespace que define el identificador**.

El valor de un identificador solamente puede interpretarse correctamente dentro del sistema que lo ha emitido o definido.

Por ejemplo:

```text
system = "https://hospital-a.example/patients"
```

o:

```text
system = "urn:health-region-x:patient-id"
```

#### `value`

```java
private final String value;
```

Representa el valor concreto asignado al paciente dentro de ese sistema.

Por ejemplo:

```text
system = "https://hospital-a.example/patients"
value  = "12345"
```

Significa:

> El paciente posee el identificador `12345` dentro del sistema de identificación definido por Hospital A.

La combinación:

```text
system + value
```

es la que proporciona contexto al identificador.

Por ejemplo:

```text
Hospital A → 12345
Hospital B → 12345
```

no representan necesariamente el mismo identificador porque pertenecen a namespaces diferentes.

Un `Patient` puede tener cero, uno o varios `Identifier`.

En futuras iteraciones el concepto podrá incorporar información adicional como tipo, emisor, periodo de validez o estado de verificación.

---

### `HumanNameTest`

`HumanNameTest` representa un nombre asociado al paciente de una forma compatible con diferentes estructuras culturales.

No se utiliza un modelo rígido como:

```text
firstName
lastName
secondLastName
```

porque esa estructura no es universal.

Actualmente contiene:

```java
private final String text;
private final String family;
private final List<String> given;
```

#### `text`

```java
private final String text;
```

Representa el **nombre completo tal y como debe mostrarse o tal y como fue registrado**, sin necesidad de dividirlo obligatoriamente en componentes.

Ejemplo:

```text
María del Carmen Pérez García
```

Esto permite conservar correctamente nombres cuya estructura no pueda dividirse fácilmente en nombre y apellido.

#### `family`

```java
private final String family;
```

Representa la parte familiar del nombre cuando esta puede identificarse.

Ejemplo:

```text
Pérez García
```

No se considera necesariamente obligatorio porque las estructuras de nombres varían entre culturas.

#### `given`

```java
private final List<String> given;
```

Representa los nombres propios conocidos.

Se utiliza una colección porque una persona puede tener uno o varios.

Ejemplo:

```text
["María", "del Carmen"]
```

Para:

```text
María del Carmen Pérez García
```

podríamos tener:

```text
text   = "María del Carmen Pérez García"
family = "Pérez García"
given  = ["María", "del Carmen"]
```

`text` conserva la representación completa original incluso cuando los componentes estructurados sean incompletos.

---

### `PatientIdentityState`

`PatientIdentityState` representa el **grado de identificación del paciente**.

Actualmente se contemplan los siguientes estados:

```java
PROVISIONAL,
DECLARED,
VERIFIED
```

#### `PROVISIONAL`

Representa un paciente cuya identidad todavía no está suficientemente determinada o confirmada.

Por ejemplo, un paciente inconsciente que llega a urgencias sin documentación.

#### `DECLARED`

Representa una identidad obtenida mediante información declarada, pero que todavía no ha sido formalmente verificada.

#### `VERIFIED`

Representa una identidad que ha sido verificada mediante los mecanismos definidos por el sistema u organización.

Este estado es independiente del ciclo de vida del registro.

Por ejemplo, un paciente puede encontrarse simultáneamente en:

```text
lifecycle     = ACTIVE
identityState = PROVISIONAL
```

---

### `PatientRecordLifecycle`

`PatientRecordLifecycle` representa el **estado administrativo del registro `Patient`**, no el estado clínico de la persona.

Actualmente dispone de:

```java
ACTIVE,
INACTIVE,
ENTERED_IN_ERROR,
MERGED
```

#### `ACTIVE`

Indica que el registro está disponible para su utilización normal.

#### `INACTIVE`

Indica que el registro continúa existiendo, pero no debería utilizarse normalmente.

#### `ENTERED_IN_ERROR`

Indica que el registro fue creado erróneamente.

#### `MERGED`

Indica que el registro ha sido fusionado con otro registro de paciente como resultado de un proceso de reconciliación de identidad.

El fallecimiento de un paciente **no equivale** a ninguno de estos estados y se modelará de forma independiente cuando se implemente esa capacidad.

---

### `BirthInformation`

`BirthInformation` representará la información disponible sobre el nacimiento del paciente.

Actualmente su implementación está pendiente porque el modelo debe ser capaz de representar correctamente diferentes grados de conocimiento.

Por ejemplo:

```text
1987-05-12
→ Fecha completa

1987-05
→ Año y mes conocidos

1987
→ Solo año conocido

Fecha aproximada
→ Se dispone de una estimación

Desconocida
→ Se ha intentado conocer el dato, pero no está disponible

No registrada
→ Todavía no se ha recopilado información
```

Por esta razón no se utilizará simplemente `LocalDate` como única representación, ya que `LocalDate` requiere una fecha completa.

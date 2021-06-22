## OOPJavaCurriculumDesign

### Roll System

A simple roll-call system with saved state which can be used in class.

More details can be found in [my blog](https://stageguard.top/OOPJavaCurriculumDesign/).

### STRUCTURE

- **Database**: Data container where stores student data, now only supports **MariaDB**.
- [**Backend**](backend): The bridge between database and frontend, handle core functions like <br>access database and
  manage roll session.
- [**Frontend-Desktop**](frontend-desktop): Frontend for native desktop integration, Windows / Linux / MacOS.
- [**Frontend-Android**](frontend-android): (WIP) Frontend for Android mobile platform.
- **Frontend-Web**: (WIP) Frontend for web, which cross-platform.

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBiYWNrZW5kKEJhY2tlbmQpXG4gICAgZnJvbnRlbmRhbmRyb2lkKFwiRnJvbnRlbmQ6IEFuZHJvaWRcIilcbiAgICBmcm9udGVuZHdlYihcIkZyb250ZW5kOiBXZWJcIilcbiAgICBmcm9udGVuZGRlc2t0b3AoXCJGcm9udGVuZDogRGVza3RvcFwiKVxuICAgIGRhdGFiYXNlKFwiRGF0YWJhc2VcIilcbiAgICBkYXRhYmFzZSAtLT4gfGFjY2Vzc3xiYWNrZW5kXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kd2ViXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kYW5kcm9pZFxuICAgIGJhY2tlbmQgLS0-IHxIVFRQIEFQSXxmcm9udGVuZGRlc2t0b3AiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid-js.github.io/mermaid-live-editor/edit/##eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBiYWNrZW5kKEJhY2tlbmQpXG4gICAgZnJvbnRlbmRhbmRyb2lkKFwiRnJvbnRlbmQ6IEFuZHJvaWRcIilcbiAgICBmcm9udGVuZHdlYihcIkZyb250ZW5kOiBXZWJcIilcbiAgICBmcm9udGVuZGRlc2t0b3AoXCJGcm9udGVuZDogRGVza3RvcFwiKVxuICAgIGRhdGFiYXNlKFwiRGF0YWJhc2VcIilcbiAgICBkYXRhYmFzZSAtLT4gfGFjY2Vzc3xiYWNrZW5kXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kd2ViXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kYW5kcm9pZFxuICAgIGJhY2tlbmQgLS0-IHxIVFRQIEFQSXxmcm9udGVuZGFuZHJvaWQiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)

### LIBRARY

- [netty](https://github.com/netty/netty) - An asynchronous event-driven network application framework for rapid
  development of maintainable high performance protocol servers & clients.
- [mysql-connector-java](https://dev.mysql.com/downloads/connector/j/) - A JDBC and X DevAPI driver for communicating
  with MySQL servers.
- [Gson](https://github.com/google/gson) - A Java serialization/deserialization library to convert Java Objects into
  JSON and back.
- [HikariCP](https://github.com/brettwooldridge/HikariCP) - A solid, high-performance, JDBC connection pool at last.
- [compose-jb](https://github.com/JetBrains/compose-jb) - Jetpack Compose for Desktop and Web, a modern UI framework for
  Kotlin that makes building performant and beautiful user interfaces easy and enjoyable.
- [decompose](https://github.com/arkivanov/Decompose) - a Kotlin Multiplatform lifecycle-aware business logic
  components (aka BLoCs) with routing functionality and pluggable UI (Android Views, Jetpack Compose, SwiftUI, JS React,
  etc.)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization) - Kotlin multiplatform / multi-format
  serialization.
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Library support for Kotlin coroutines
  with [multiplatform](https://github.com/Kotlin/kotlinx.coroutines#multiplatform) support.

### DEPLOY

1. Clone this repository using [IntelliJ IDEA](https://www.jetbrains.com/idea/).
2. Change default property
   at [ApplicationMain.java](backend/src/main/java/me/stageguard/oopcd/backend/ApplicationMain.java) in backend
   and [RollManager.kt](frontend-desktop/src/main/kotlin/me/stageguard/oopcd/frontend/desktop/core/RollManager.kt) in
   frontend-desktop.

```java
//ApplicationMain.java
var nettyHttpService=NettyHttpServerBuilder
        .create(8088) // http server port
        .route(/* ... */)
        .authKey("114514_1919810") // authentication key that required in http headers.
        .build();
        var databaseService=DatabaseBuilder
        .create("localhost",3306) // address and port of MariaDB Databaes
        .username("root") // username
        .password("testpwd") // password
        .database("oopcd_database") // database table
        .build();
```

```kotlin
object RollManager {
    private const val AUTH_KEY = "114514_1919810" // authentication key that similar to .authkey()
    private const val BASE_URL = "http://localhost:8088" // address of http server
    /* ... */
}
```

3. Run Gradle Task `backend -> shadow -> shadowJar` and `frontend-desktop -> shadow -> shadowJar`.<br>The shadowed jar
   file can be found in `backend/build/libs/backend-x.x-all.jar` and `frontend-desktop/build/libs/frontend-x.x-all.jar`.

4. Execute these jars.

```bash
java -jar backend-x.x-all.jar # run backend service
java -jar frontend-desktop-x.x-all.jar # run frontend application
```

> If you don't want to change these default config and build jars manually, you can simplify download the jars in [releases page](https://github.com/StageGuard/OOPJavaCurriculumDesign/releases).

### LICENSE

    RollCallSystem
    Copyright (C) 2021 StageGuard
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
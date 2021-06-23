## OOPJavaCurriculumDesign

### Roll System

A simple roll-call system with saved state which can be used in class.

More details can be found in [my blog](https://stageguard.top/OOPJavaCurriculumDesign/).

### Project Structure

- **Database**: Data container where stores student data, now only supports **MariaDB**.
- [**Backend**](backend): The bridge between database and frontend, handle core functions like <br>access database and
  manage roll session.
- [**Frontend-Desktop**](frontend-desktop): Frontend for native desktop integration, Windows / Linux / MacOS.
- [**Frontend-Android**](frontend-android): (WIP) Frontend for Android mobile platform.
- **Frontend-Web**: (WIP) Frontend for web, which cross-platform.

[![](https://mermaid.ink/img/eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBiYWNrZW5kKEJhY2tlbmQpXG4gICAgZnJvbnRlbmRhbmRyb2lkKFwiRnJvbnRlbmQ6IEFuZHJvaWRcIilcbiAgICBmcm9udGVuZHdlYihcIkZyb250ZW5kOiBXZWJcIilcbiAgICBmcm9udGVuZGRlc2t0b3AoXCJGcm9udGVuZDogRGVza3RvcFwiKVxuICAgIGRhdGFiYXNlKFwiRGF0YWJhc2VcIilcbiAgICBkYXRhYmFzZSAtLT4gfGFjY2Vzc3xiYWNrZW5kXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kd2ViXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kYW5kcm9pZFxuICAgIGJhY2tlbmQgLS0-IHxIVFRQIEFQSXxmcm9udGVuZGRlc2t0b3AiLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9LCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)](https://mermaid-js.github.io/mermaid-live-editor/edit/##eyJjb2RlIjoiZ3JhcGggTFJcbiAgICBiYWNrZW5kKEJhY2tlbmQpXG4gICAgZnJvbnRlbmRhbmRyb2lkKFwiRnJvbnRlbmQ6IEFuZHJvaWRcIilcbiAgICBmcm9udGVuZHdlYihcIkZyb250ZW5kOiBXZWJcIilcbiAgICBmcm9udGVuZGRlc2t0b3AoXCJGcm9udGVuZDogRGVza3RvcFwiKVxuICAgIGRhdGFiYXNlKFwiRGF0YWJhc2VcIilcbiAgICBkYXRhYmFzZSAtLT4gfGFjY2Vzc3xiYWNrZW5kXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kd2ViXG4gICAgYmFja2VuZCAtLT4gfEhUVFAgQVBJfGZyb250ZW5kYW5kcm9pZFxuICAgIGJhY2tlbmQgLS0-IHxIVFRQIEFQSXxmcm9udGVuZGFuZHJvaWQiLCJtZXJtYWlkIjoie1xuICBcInRoZW1lXCI6IFwiZGVmYXVsdFwiXG59IiwidXBkYXRlRWRpdG9yIjpmYWxzZSwiYXV0b1N5bmMiOnRydWUsInVwZGF0ZURpYWdyYW0iOmZhbHNlfQ)

### Deploy

1. Download the prebuilt executable files
   in [releases page](https://github.com/StageGuard/OOPJavaCurriculumDesign/releases) or [build manually](#BUILD) on
   your own.

2. Set property via JVM property.

   **For backend:**

    - `sg.oopcd.backend.server.port` : http server port. (default: `8081`)
    - `sg.oopcd.backend.server.authKey` : auth key. (default: `114514_1919810`)
    - `sg.oopcd.backend.database.address` : database address. (default: `localhost`)
    - `sg.oopcd.backend.database.port` : database port. (default: `3306`)
    - `sg.oopcd.backend.database.username` : database username to connect. (default: `root`)
    - `sg.oopcd.backend.database.password` : the user's password. (default: `testpwd`)
    - `sg.oopcd.backend.database.table` : database table that backend access.(default: `oopcd_database`)

   **For frontend-desktop:**

    - `sg.oopcd.frontend.desktop.server.address` : http server address. (default: `localhost`)
    - `sg.oopcd.frontend.desktop.server.port` : http server port, must be same with `sg.oopcd.backend.server.port`. (
      default: `8081`)
    - `sg.oopcd.frontend.desktop.authKey` : auth key, must be same with `sg.oopcd.backend.server.authKey`. (
      default: `114514_1919810`)

3. Execute jars with your custom property.

```bash
java -Dsg.oopcd.backend.server.port=11451 -D... -jar backend-x.x-all.jar # run backend service
java -Dsg.oopcd.frontend.desktop.server.port=11451 -D... -jar OOPCDFrontendDesktop-platform-arch-x.x.x.jar # run frontend application
```

### Build

1. Clone this repository using [IntelliJ IDEA](https://www.jetbrains.com/idea/).
2. Run Gradle task `backend:shadowJar` and `frontend-desktop:packageUberJarForCurrentOS`(
   or `frontend-desktop:createDistributable`).<br>The shadowed jar file can be found
   in `backend/build/libs/backend-x.x-all.jar`
   and `frontend-desktop/build/compose/jars/OOPCDFrontendDesktop-platform-arch-x.x.x.jar`(
   or `frontend-desktop/build/compose/binaries/main/app/OOPCDFrontendDesktop` if you runs `createDistributable`).

### Library

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

### Thanks to

Thanks [JetBrains](https://www.jetbrains.com/?from=stageguard-oopcd) for providing free license
for [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=stageguard-oopcd) and other IDEs for open source project.

[<img src="docs/article/image/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=stageguard-oopcd)

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
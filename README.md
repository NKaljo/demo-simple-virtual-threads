# demo-simple-virtual-threads

A minimal Spring Boot demo comparing Java 21 Virtual Threads vs. traditional platform threads for handling blocking I/O.

## Modules

| Module | Port | Role |
|--------|------|------|
| `vt-main` | 8080 | Client service — receives requests, calls vt-blocker, logs thread info |
| `vt-blocker` | 8085 | Backend service — simulates blocking I/O via `Thread.sleep()` |

## How it works

`vt-main` exposes `GET /block/{seconds}`. Each request blocks for the given number of seconds by calling `vt-blocker`, then returns the current thread's `toString()`. With Virtual Threads enabled, Tomcat can serve many concurrent requests without exhausting a fixed thread pool.

## Running

Start both services (in separate terminals):

```bash
# Terminal 1
cd vt-blocker && mvn spring-boot:run

# Terminal 2
cd vt-main && mvn spring-boot:run
```

## Virtual Threads toggle

Edit `vt-main/src/main/resources/application.properties`:

```properties
# Virtual Threads ON (default)
spring.threads.virtual.enabled=true

# Virtual Threads OFF — uncomment below and comment out the line above
#server.tomcat.threads.max=10
#spring.threads.virtual.enabled=false
```

Limiting `server.tomcat.threads.max` to a small number makes the contrast visible: with platform threads, concurrent requests queue up; with Virtual Threads, they all proceed.

## Load testing

Use [Apache Bench](https://httpd.apache.org/docs/2.4/programs/ab.html) to send concurrent requests:

```bash
ab -n 60 -c 20 http://localhost:8080/block/3
```

- `-n 60` — total 60 requests
- `-c 20` — 20 concurrent requests
- `/block/3` — each request blocks for 3 seconds on the backend

**With Virtual Threads** (`spring.threads.virtual.enabled=true`): all 20 concurrent requests are handled immediately; total time ≈ 3 × ceil(60/20) = ~9 s.

**Without Virtual Threads** (e.g. `server.tomcat.threads.max=10`): only 10 requests run at a time; the rest queue, and total time roughly doubles.

Check the `vt-main` logs to see thread names — Virtual Threads show as `VirtualThread[...]` while platform threads show as `Thread[...]`.

## Requirements

- Java 21+
- Maven 3.9+

# Notes App

A native Android application for creating and organising notes. The app is a REST
client: notes are stored on a server and accessed over HTTP rather than kept in a
local database.

## Features

- Create, edit and delete notes, each with a title and body.
- Assign a colour to a note; the colour is shown in the list.
- Mark notes as favourites and view them on a separate screen.
- Search notes by keyword.
- List rendered with `RecyclerView` and a custom adapter.

## Tech stack

Java, Android SDK (compileSdk 35, minSdk 26, targetSdk 35), Retrofit 2 with the
Gson converter for HTTP and JSON, Material Components, RecyclerView, XML layouts.
Built with Gradle using Kotlin DSL build scripts.

## Backend

The application talks to a small REST API which is not part of this repository.
`RetrofitClient` points at:

```
http://10.0.2.2/notesapi/
```

`10.0.2.2` is the address the Android emulator uses to reach `localhost` on the
host machine, so the API is expected to run locally during development.

The `NotesApi` interface expects these endpoints:

| Method | Endpoint             | Parameters                          |
|--------|----------------------|-------------------------------------|
| GET    | `get_notes.php`      | none, returns a JSON array of notes |
| POST   | `add_note.php`       | `title`, `content`, `color`         |
| POST   | `update_note.php`    | `id`, `title`, `content`, `color`   |
| POST   | `delete_note.php`    | `id`                                |
| POST   | `update_favorite.php`| `id`, `is_favorite`                 |

POST requests are form URL encoded. A note is returned as an object with `id`,
`title`, `content`, `color` and `is_favorite`.

## Getting started

Prerequisites: Android Studio, a device or emulator running API 26 or higher, and
a server exposing the endpoints above.

1. Clone the repository and open the `NotesApp` folder in Android Studio.
2. Let Gradle sync and download dependencies.
3. Start the backend so it is reachable at the base URL above. To point at a
   different host, change `BASE_URL` in
   `app/src/main/java/com/example/notesapp2/RetrofitClient.java`.
4. Run the app on an emulator or device.

## Notes

Requests are made with Retrofit's asynchronous `enqueue`, so callbacks return on
the main thread and network work stays off it. Because there is no local cache,
the application requires a reachable backend to display any data.

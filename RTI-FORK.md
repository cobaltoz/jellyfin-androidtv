# Jellyfin Android TV - RTI fork

Personal fork adding remote "jump to letter" for the library grid, driven from an RTI
control system through the Jellyfin server.

## Remote command

Send a SendString general command to the Shield's Jellyfin session:

    POST /Sessions/{sessionId}/Command
    {"Name":"SendString","Arguments":{"String":"letter:A"}}

| String              | Effect                                                       |
|---------------------|--------------------------------------------------------------|
| `letter:A` .. `Z`   | Grid filtered to titles starting with that letter            |
| `letter:#`          | Titles starting with a digit or symbol                       |
| `letter:all`        | Clear the letter filter                                      |
| `letter:next/prev`  | Step to the next / previous letter                           |
| `letter:pick`       | Pop up the on-screen A-Z picker                              |
| `...:movies` / `...:tvshows` | Force that library (e.g. `letter:B:tvshows`)        |

If a library grid is already on screen the letter applies to it; otherwise the Movies
library grid (or TV Shows with `:tvshows`) opens at that letter. Ignored during playback.
Letters use Jellyfin's sort name, so "The Matrix" is under M. A remote letter switches the
grid to Name sort until you pick another sort from the menu.

## Install

The app id is `org.jellyfin.androidtv.rti` ("Jellyfin RTI"), so it installs alongside the
official app. Every push to the `rti` branch builds a signed APK into Releases.

package org.jellyfin.androidtv.rti

/**
 * RTI fork: remote "jump to letter" for library grids.
 *
 * A server SendString command of the form
 *   letter:<L>[:movies|tvshows]
 * where <L> is A-Z, "#", "all", "next", "prev" or "pick", is routed here by SocketHandler.
 * The visible BrowseGridFragment (if any) registers itself as [listener] while started.
 */
object RemoteLetter {
	const val PREFIX = "letter:"
	val LETTERS: List<String> = listOf("#") + ('A'..'Z').map { it.toString() }

	fun interface Listener {
		/** @return true if the visible grid handled it, false to let the caller navigate. */
		fun onRemoteLetter(letter: String, libraryType: String?): Boolean
	}

	@Volatile
	var listener: Listener? = null

	data class Command(val letter: String, val libraryType: String?)

	/** Parse "letter:A", "letter:#:movies", "letter:next" etc. Returns null when not a letter command. */
	fun parse(raw: String?): Command? {
		val text = raw?.trim() ?: return null
		if (!text.startsWith(PREFIX, ignoreCase = true)) return null
		val parts = text.substring(PREFIX.length).split(':').map { it.trim() }
		val l = parts.getOrNull(0)?.takeIf { it.isNotEmpty() } ?: return null
		val letter = when {
			l.equals("all", true) -> "all"
			l.equals("next", true) -> "next"
			l.equals("prev", true) -> "prev"
			l.equals("pick", true) -> "pick"
			l == "#" || l.equals("num", true) -> "#"
			l.length == 1 && l[0].uppercaseChar() in 'A'..'Z' -> l.uppercase()
			else -> return null
		}
		val type = parts.getOrNull(1)?.lowercase()?.takeIf { it == "movies" || it == "tvshows" }
		return Command(letter, type)
	}

	/** Step from the current letter (null = all) to the next/previous one. */
	fun step(current: String?, forward: Boolean): String {
		val i = if (current == null) -1 else LETTERS.indexOf(current.uppercase())
		val n = if (forward) i + 1 else i - 1
		return when {
			n < 0 -> LETTERS.last()
			n >= LETTERS.size -> LETTERS.first()
			else -> LETTERS[n]
		}
	}
}

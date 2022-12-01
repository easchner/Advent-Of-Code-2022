import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInputString(name: String) = File("src", "$name.txt")
    .readLines()

fun readInputLong(name: String) = File("src", "$name.txt")
    .readLines()
    .map { it.toLong() }

// For inputs with empty lines breaking up groups, this will return a list of groups
fun readInputSpaceDelimited(name: String): List<List<String>> {
    val groups = File("src", "$name.txt").readText().replace("\r", "").split("\n\n")
    return groups.map { it.split("\n") }
}

fun readAndCompoundSpaceDelimited(name: String, compounder: (List<String>) -> Long): List<Long> {
    val groups = readInputSpaceDelimited(name)
    return groups.map { compounder(it) }
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

package preponderous.ponder.command

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test

class ArgsTests {

    @Test
    fun `dropFirst drops first argument`() {
        assertArrayEquals(arrayOf("def", "ghi", "jkl"), arrayOf("abc", "def", "ghi", "jkl").dropFirst())
    }

    @Test
    fun `dropFirst on a single argument yields an empty array`() {
        assertArrayEquals(emptyArray<String>(), arrayOf("abc").dropFirst())
    }

    @Test
    fun `dropFirst on no arguments yields an empty array`() {
        assertArrayEquals(emptyArray<String>(), emptyArray<String>().dropFirst())
    }

    @Test
    fun `unquote parses quoted args`() {
        assertArrayEquals(
            arrayOf("abc", "def ghi", "jkl", "mno pqr stu"),
            arrayOf("abc", "\"def", "ghi\"", "\"jkl\"", "\"mno", "pqr", "stu\"").unquote()
        )
    }

    @Test
    fun `nested quotes are left`() {
        assertArrayEquals(
            arrayOf("abc", "def \"ghi\"", "jkl", "\"mno pqr\" stu"),
            arrayOf("abc", "\"def", "\"ghi\"\"", "\"jkl\"", "\"\"mno", "pqr\"", "stu\"").unquote()
        )
    }

    @Test
    fun `unquote leaves unquoted args untouched`() {
        assertArrayEquals(arrayOf("abc", "def"), arrayOf("abc", "def").unquote())
    }

    @Test
    fun `unquote on no arguments yields an empty array`() {
        assertArrayEquals(emptyArray<String>(), emptyArray<String>().unquote())
    }

    @Test
    fun `an unclosed quote merges every following argument`() {
        assertArrayEquals(arrayOf("abc def"), arrayOf("\"abc", "def").unquote())
    }

    // Regression coverage for https://github.com/Dans-Plugins/Ponder/issues/137.

    @Test
    fun `a lone quote is an unterminated quote that merges every following argument`() {
        assertArrayEquals(arrayOf(""), arrayOf("\"").unquote())
        assertArrayEquals(arrayOf("abc def"), arrayOf("\"", "abc", "def").unquote())
    }

    @Test
    fun `a pair of quotes is an empty argument`() {
        assertArrayEquals(arrayOf(""), arrayOf("\"\"").unquote())
        assertArrayEquals(arrayOf("abc", "", "def"), arrayOf("abc", "\"\"", "def").unquote())
    }

    @Test
    fun `an unpaired closing quote is stripped`() {
        assertArrayEquals(arrayOf("abc"), arrayOf("abc\"").unquote())
    }

    @Test
    fun `an unpaired closing quote does not break the following group`() {
        assertArrayEquals(
            arrayOf("abc", "def ghi"),
            arrayOf("abc\"", "\"def", "ghi\"").unquote()
        )
    }
}

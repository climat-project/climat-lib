import kotlin.test.Test
import kotlin.test.assertContentEquals

class TestExecution {

    private val json = """{
      "name": "cli-alias-aggregator",
      "description": "",
      "action": "",
      "children": [
        {
          "name": "new",
          "description": "Creates a new cli",
          "parameters": [
            "opt:flag:interactive:i"
          ],
          "action": "echo 'abcd'",
          "children": [
            {
              "name": "template",
              "paramDefaults": {
                "param1": "default"
              },
              "parameters": [
                "opt:arg:param1",
                "opt:arg:param2"
              ],
              "action": "echo '$(interactive)' '$(param1)'"
            }
          ]
        },
        {
          "name": "renew",
          "description": "Updates an existing cli with the new json file",
          "action": "echo 'qwe'"
        },
        {
          "name": "remove",
          "parameters": [
            "opt:flag:force:f"
          ],
          "description": "Removes an existing cli",
          "action": "echo 'what ever'"
        },
        {
          "name": "export",
          "description": "Exports the cli",
          "parameters": [
            "opt:flag:type:t"
          ],
          "action": "echo 'abcd'"
        }
      ]
    }"""

    private fun exec(args: String): String? {
        var ans: String? = null
        ToolchainProcessor(json, {
            ans = it
        }).execute(args)
        return ans
    }

    @Test
    fun testHappyFlow() {
        val executed = listOf(
            "new",
            "new --interactive template --param1 abc",
            "new template --param2 we",
            "new --interactive template --param2 we --param1 nondefault"
        )
            .map(::exec)
            .toTypedArray()
        println(executed)
        assertContentEquals(
            arrayOf(
                "echo 'abcd'",
                "echo 'true' 'abc'",
                "echo 'false' 'default'",
                "echo 'true' 'nondefault'"
            ),
            executed
        )
    }
}

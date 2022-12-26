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
          "params": {
            "interactive": "opt:flag:i"
          },
          "action": "echo 'abcd'",
          "children": [
            {
              "name": "template",
              "params": {
                "param1": "opt:arg",
                "param2": "opt:arg"
              },
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
          "params": {
            "force": "opt:flag:f"
          },
          "description": "Removes an existing cli",
          "action": "echo 'what ever'"
        },
        {
          "name": "export",
          "description": "Exports the cli",
          "params": {
            "type": "opt:flag:t"
          },
          "action": "echo 'abcd'"
        }
      ]
    }"""

    private fun exec(args: String): String? {
        var ans: String? = null
        ToolchainProcessor(json) {
            ans = it
        }.executeFromString(args)
        return ans
    }

    @Test
    fun testHappyFlow() {
        val executed = listOf(
            "new",
            "new --interactive template --param1 abc",
            "new template --param2 we"
        )
            .map(::exec)
            .toTypedArray()
        println(executed)
        assertContentEquals(
            arrayOf(
                "echo 'abcd'",
                "echo 'true' 'abc'",
                "echo 'null' 'null'"
            ),
            executed
        )
    }
}

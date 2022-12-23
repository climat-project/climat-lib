import kotlinx.cli.ArgParser
import kotlinx.serialization.*
import kotlinx.serialization.json.*

fun main(args: Array<String>) {
    println(process.cwd())
    val jsonString = fs.readFileSync("../../../../src/main/resources/toolchain-example.json", "utf8")
    val obj = Json.decodeFromString<Toolchain>(jsonString)

    ToolchainProcessor(obj).execute(arrayOf(
        "new",
        "--interactive",
        "template",
        "what",
        "whaat"
    ))

}


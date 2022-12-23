import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    println(process.cwd())
    val jsonString = fs.readFileSync("../../../../src/main/resources/toolchain-example.json", "utf8")
    val obj = Json.decodeFromString<Toolchain>(jsonString)

    ToolchainProcessor(obj).execute(
        arrayOf(
            "new",
            "--interactive",
            "template",
            "what",
            "whaat"
        )
    )
}

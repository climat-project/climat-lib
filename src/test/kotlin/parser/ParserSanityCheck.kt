package parser

import parser.dsl.decodeCliDsl
import kotlin.test.Test

class ParserSanityCheck {

    val cliDsl = """
        root {
            const rootConst = "rootConst"
            children [
                fe() {
                    aliases [alias1, alias3]
                },
                sealed js {},
                ""${'"'}
                abc
                @param p1 doc
                ""${'"'}
                c3(p1 s: arg, p2 1: flag, p3?: arg, p4?: flag,
                   p5?: arg = "wat",
                   p6?: flag = false) {
                   const myConst = "abc $(p1) cde"
                   action "random action"
                },
                
                c4 {
                    action scope params
                    
                    children [
                        sealed aa{
                            const ae = "wat"
                            const be = false
                            override default p1 = "w"
                        },
                        shifted bb{
                        },
                        sealed shifted cc{
                            action <random script>
                        },
                        shifted sealed dd{}
                    ]
                }
            ]
        }
    """

    @Test
    fun test() {
        decodeCliDsl(cliDsl)
    }
}

package parser

import com.climat.library.parser.dsl.decodeCliDsl
import kotlin.test.Test

class ParserSanityCheck {

    val cliDsl = """
        root {
            const rootConst = "rootConst"
            children [
                sub fe() {                           // Single line Comment
                    aliases [alias1, alias3]     
                }
                sealed sub js {}
                
                /* Multi
                Line
                Comment 
                */
                
                ""${'"'}
                abc
                @param p1 doc
                ""${'"'}
                sub c3(p1 s: arg, p2 1: flag, p3?: arg, p4?: flag,
                   p5?: arg = "wat",
                   p6?: flag = false) {
                   const myConst = "abc $(p1) cde"
                   action "random action"
                }
                
                sub c4 {
                    action scope params
                    
                    children [
                        sealed sub aa{
                            const ae = "wat"
                            const be = false
                            override default p1 = "w"
                        }
                        shifted sub bb{
                        }
                        sealed shifted sub cc{
                            action <random script>
                        }
                        shifted sealed sub dd{}
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

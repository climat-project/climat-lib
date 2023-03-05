package parser

import com.climat.library.dslParser.dsl.decodeCliDsl
import kotlin.test.Test

class ParserSanityCheck {

    val cliDsl = """
        @alias(root2)
        root {
            const rootConst = "rootConst"
            
            @aliases(alias1 alias3)
            sub fe() {                           // Single line Comment
            }
            
            @seal sub js {}
            
            /* Multi
            Line
            Comment 
            */
            
            ""${'"'}
            abc
            @param p1 doc
            ""${'"'}
            sub c3(p1 s: arg, p2 1: flag, p3: arg?, p4: flag,
               p5: arg? = "wat",
               p6: flag) {
               const myConst = "abc $(p1) cde"
               action "random action"
            }
            
            sub c4 {
                action scope params
                
                @seal sub aa{
                    const ae = "wat"
                    const be = false
                }
                @shift sub bb{
                }
                @seal @shift sub cc{
                    action <random script>
                }
                @shift @seal sub dd{}
            }
        }
    """

    @Test
    fun test() {
        decodeCliDsl(cliDsl)
    }
}

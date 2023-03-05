package e2e.features

import com.climat.library.commandParser.parse
import com.climat.library.validation.validate
import kotlin.test.Test

class CustomScript {

    @Test
    fun testCompiles() {
        validate(
            parse(
                """
                hello-world(location l: arg) {
    
                    action js <
                        console.log("Hello World from a JavaScript environment !");
                        console.log("It seems that you are situated in ", params.location);
                        console.log("Is that correct?");
                    >
                    
                }
                """
            )
        )
    }

    @Test
    fun escapeSequence() {
        validate(
            parse(
                """
                    hello-world {
                    
                        // The ">>" gets escaped to just ">"
                        action js <
                            const a = 3
                            const b = 2
                            if (a >> b) { console.log("a is greater than b") } 
                        >
                        
                    }
                """
            )
        )
    }
}

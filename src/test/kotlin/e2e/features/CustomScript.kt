package e2e.features

import com.climat.library.commandParser.getValidations
import com.climat.library.commandParser.parse
import kotlin.test.Test

class CustomScript {

    @Test
    fun testCompiles() {
        getValidations(
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
}

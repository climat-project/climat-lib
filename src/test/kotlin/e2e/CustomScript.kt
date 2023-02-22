package e2e

import com.climat.library.toolchain.parse
import com.climat.library.toolchain.validate
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
}

# Roadmap ordered by priority and feasibility

- Being able to pass an object to an action handler, not only a template string how is now currently
- More unit tests
- Offloading unknown parameters to the command
- Check if we can replace kotlinx-cli library with Clikt (https://ajalt.github.io/clikt/)
- Multiple names for one subcommand (multiple names or one name with more aliases?)
- Plugins
- Beautify errors and ensure proper error handling
- Object-Oriented API
- Ability to autogenerate shorthand for params
- Ability to generate a completely interactive toolchain<br/> 
eg. if you missed a param, the cli should ask for it in a separate prompt.<br/>
eg. if you missed a subcommand, you should be able to menu-pick between the available options
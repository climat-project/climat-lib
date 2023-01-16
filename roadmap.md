# Roadmap ordered by priority and feasibility

- ~~More unit tests~~
- ~~Constants~~
- ~~Multiple names for one subcommand (one name with more aliases?)~~
- Reference resources, which will be copied when the alias is installed
- Check if we can replace kotlinx-cli library with Clikt (https://ajalt.github.io/clikt/)
- Toolchain name/alias or param/constant name pattern (only alphanumeric + dash + underscore for example)
- Appending (Offloading) unknown parameters to the template end
- Being able to pass an object to an action handler, not only a template string how is now currently
- Plugins
- Beautify errors and ensure proper error handling
- Object-Oriented API
- Windows?/BSD/GNU style cli
- Ability to autogenerate shorthand for params<br/>
Check if we even want that feature which could lead to problems. For instance, if we add another paramter, all the other short hands could be recalculated
- Ability to generate a completely interactive toolchain<br/> 
eg. if you missed a param, the cli should ask for it in a separate prompt.<br/>
eg. if you missed a subcommand, you should be able to menu-pick between the available options

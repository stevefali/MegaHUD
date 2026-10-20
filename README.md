# MegaHUD

## A HUD Plugin + API for my Mega Randomizer Server Spigot Server

### I plan to write a proper README later, but for now, I have pasted in my project overview notes:

Like in many other Minecraft servers, the Sidebar is a handy place to display information to the user.
The vanilla sidebar, though is a bit of an awkward way to do that, but with NMS, there are a lot more formatting options.
Also, having the Bukkit server manage sidebars for every player is a bit messy.

What I found to be a much better system is to just send client sidebar packets directly myself instead of using the vanilla system.
This again seemed like a good thing to package into its own plugin/API, so I created MegaHUD.
MegaHUD has a similar basic structure to MegaNPCs: a plugin to do all the work, with an API that exposes a minimal amount of code through an interface, which I called `MegaHudService`.

As I mentioned, the vanilla Sidebar doesn't give you very much control over the look of the sidebar. You can't change the color of scores, and every line in the sidebar must show a numeric score on the right hand side. Also, the vanilla sidebar automatically rearranges the order of the lines in descending order of score.

However, in NMS, there is a `NumberFormat` class that allows you to format the numerical score value, leave it blank, or display text in its place.

So taking advantage of that, my API allows consuming plugins to easily create sidebar lines with text on the left, text on the right, both, or to make a completely blank line.
After some fiddling, I decided that in cases where I do want to display a numerical score on the right, it is better to actually just have the number in a String.

- Again, this is because the client will reorder the lines based on the numerical score, which I found a bit annoying.
    - Using Strings instead allows me to get around this issue.

    - To allow the consuming plugin to directly control the order of the sidebar lines, MegaHUD has the consuming plugin set a 0-based index on each sidebar line, with the order of the lines corresponding to the ascending value of the index.
        - This is done by assigning the line's score value based on the index value in the background. (In reverse by using an arbitrary value of `1000 - index` because the sidebar normally puts the lines in DESCENDING order).
        - The "index"/"score" value is never shown to the user because I force the right side of the sidebar lines to be either a String or blank.

  Unlike MegaNPCs, which only exposes vanilla Spigot types in the API, MegaHUD adds a small `SidebarLine` class that I created, with static constructor methods, such as `leftSideOnly`, `rightSideOnly`, and `bothSides`, which return SidebarLines with text on the left side, right side, and both sides respectively. It also has a static `BLANK` member for blank lines.

    - This makes it easy for consuming plugins to set sidebar lines without having to worry about any of the implementation details.
        - The API is intended to be as idempotent as possible, so that consuming plugins can mostly just set sidebar lines based on their index at any time without having to worry about removing or changing values of pre-existing lines.

        - Most of that was easy enough to implement because of the way the relevant NMS packets work.

            - However, when it comes to creating/updating the overall sidebar itself, which requires the `ClientboundSetObjectivePacket`, you have to be careful, because the "create" type is separate from the "update" type, and if you send the "create" method code when the sidebar objective already exists on the client, it will cause an error that will kick the player!!!

                - To prevent that from happening, MegaHUD tracks a boolean `isSidebarObjectiveSent` for each player, and then sets that "method" code accordingly, i.e., "if the objective has already been sent to the client, use the 'update' code, else use the 'create' code".

  I quite like this system, because it strictly uses packets directly and does not allow the internal Spigot server to manage (or even really be aware of) the sidebars, which gives me complete control, avoiding all the weirdness of the vanilla sidebar.

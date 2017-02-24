# Restify
Restify is a desktop Java application intended as an HTTP Load Tester for a REST API.  It has these features:

* Store a list of servers commonly used
* Supports authentication (using a session cookie for subsequent API calls)
* Use one or more URLs, simulating multiple threads ("users") and repeat runs
* Variable delay between each URL call (to simulate real-world usage)
* Import a list of URLs from a HAR session (e.g., from Chrome's "Network" tab)
* Record the response time for each API call
* Record the best and worst response times
* Record the worst-performing URL

The persisted data is saved to a properties file.  No database is required.

More information on each screen is included below.

# "Servers" Screen

This screen is used to add, edit and delete servers used by the application.  These are the controls:

* Drop-down list box containing the names of saved servers
* "Select" button to make it the active server (used for logins and performance runs)
* Display the URL of the selected server (the format is "http://example.com", or using "https" for the protocol)
* "Add" button to add a new server (name and URL)
* "Edit" button to edit an existing server's information
* "Delete" button to remove the server

# "Login" Screen

Since most REST APIs require authentication, this screen is used to provide login credentials.
Typical steps are:

* Select a server from the "Servers" tab
* Enter a username and password on the "Login" tab
* Leave the "Auto Sign-In" checkbox checked (this causes the application to first sign-in
  using these credentials when starting a performance run)

The fields are:

* URL of the selected server, with "/login" appended by default (can be modified)
* "Auto Sign-In" checkbox (used by the performance run if the user is not logged in; uncheck
if authentication is not required)
* Text field to enter the user's login name
* Text field to enter the user's password
* "Sign In" button (to test the credentials)
* "Sign Out" button (to close a session)
* The session_cookie value returned on successful login
* "Copy" button to copy the cookie to the clipboard
* The request and response on login attempt (included for informational purposes)

Some notes:

* You can test the credentials via the "Sign In" button
* If login fails, an error message displays
* If login succeeds, the server URL is displayed in the bottom-left corner, and the user
name is displayed in the bottom-right corner
* Authentication assumes this is performed via POST <server>/login with a
  payload of "username=ABC&password=XYZ" (this can be modified in LoginTab.java)
* If the authentication is successful, the server will return a Set-Cookie imperative in
  the response header, which is used automatically by the Unirest library, so the API
  calls made during a performance run do not need to have the cookie set automatically
* The screen will show the session_cookie field returned by the login call; this is just
  for informational purposes, and is not needed elsewhere

# "Performance" Screen

This screen contains a number of input and output fields.  The input fields are:

* The list of URLs to use for the performance test (any lines starting with ";" are skipped)
* "Min Delay": the minimum amount of time to wait before proceeding to the next URL in the list during a run
* "Max Delay": the maximum amount of time to wait before proceeding to the next URL in the list during a run
* "# Threads": the number of users to simulate (one thread per user)
* "# Runs": the number of runs to perform for each user
* "Failure Threshold": if an API call takes longer than this limit, it's considered a performance failure;
  used to compute the "Failure Rate" in the output section

The buttons are:

* "Start": start a performance run
* "Stop": stop a performance run
* "Reset": set the inputs to the defaults, and clear the output
* "Import HAR": import a set of URLs from a HAR session (e.g., from Chrome); only URLs starting with
  the selected server name are used (the application also discards some URLs associated with JS,
  CSS, images, etc.)

The output fields are:

* List of completed URLs, with the thread number, run number, URL, and elapsed time (in milliseconds)
* The progress (a percentage of completed URLs)
* The failure rate (failed calls / completed calls)
* The minimum response time of all calls (milliseconds)
* The maximum response time of all calls (milliseconds)
* The worst performing URL (the one with the highest response time, for any user or run)

The total number of API calls made is a function of: # threads * # runs * # URLs (plus one for the initial login call, if needed)

# "Console" Screen

This screen simply shows information gathered during a performance run:

* Date / time stamp
* Thread number
* Run number
* URL
* HTTP status code and description (e.g., "200: OK")

The screen contains a "Clear" button to clear the logged information.

# Additional Information

Nemo requires Java 8 or later to build and execute.

There is currently no help file, but there is tooltip text for most of the controls, so the interface should be easy to understand.

To run the appication, build it via Ant ('ant clean dist'), and then open via 'java -jar restify.jar' (or double-click nemo.jar).
The required dependencies need to be in the same folder as restify.jar (see the next paragraph).

The required dependencies are in the libs folder, consisting of Unirest (HTTP client), JSON and their respective dependencies.
These libraries are copyright their respective owners.

The source code is released under the MIT license, with the exception of the included JAR libraries.
